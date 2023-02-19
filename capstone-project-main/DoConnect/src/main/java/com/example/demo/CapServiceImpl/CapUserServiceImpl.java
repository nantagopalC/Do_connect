package com.example.demo.CapServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import javax.persistence.EntityManager;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.CapEntity.CapAdmin;
import com.example.demo.CapEntity.Answer;
import com.example.demo.CapEntity.CapImage;
import com.example.demo.CapEntity.Question;
import com.example.demo.CapModelDTO.AlreadyThere;
import com.example.demo.CapModelDTO.CapAnswerDTO;
import com.example.demo.CapModelDTO.CapQuestionDTO;
import com.example.demo.CapModelDTO.NotFound;
import com.example.demo.CapRepository.CapAdminRepository;
import com.example.demo.CapRepository.CapAnswerRepository;
import com.example.demo.CapRepository.CapImageModelRepository;
import com.example.demo.CapRepository.CapQuestionRepository;
import com.example.demo.CapRepository.CapUserRepository;
import com.example.demo.CapService.CapEmailService;
import com.example.demo.CapService.CapUserService;
import com.example.demo.CapModelDTO.CapMessageDTO;
import com.example.demo.CapEntity.CapUser;

@Service
public class CapUserServiceImpl implements CapUserService {
	@Autowired
	private CapUserRepository userRepo;
	@Autowired
	private CapAdminRepository capAdminRepository;
	@Autowired
	private CapQuestionRepository questionRepo;
	@Autowired
	private CapAnswerRepository answerRepo;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private CapImageModelRepository imageModelRepo;
	@Autowired
	private CapEmailService capEmailService;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public CapUser userLogin(String email, String password) {
		CapUser capUser = userRepo.findByEmail(email);
		if (Objects.isNull(capUser))
			throw new NotFound();

		if (capUser.getPassword().equals(password)) {
			capUser.setIsActive(true);
			userRepo.save(capUser);
		} else
			throw new NotFound();
		return capUser;
	}

	@Override
	public String userLogout(Long userId) {
		CapUser capUser = userRepo.findById(userId).orElseThrow(() -> new NotFound("User Not Found" + userId));
		capUser.setIsActive(false);
		userRepo.save(capUser);
		return "Logged Out";
	}

	@Override
	public CapUser userRegister(CapUser capUser) {
		CapUser user1 = userRepo.findByEmail(capUser.getEmail());
		if (Objects.isNull(user1))
			return userRepo.save(capUser);
		throw new AlreadyThere();
	}

	@Override
	public Question askQuestion(CapQuestionDTO capQuestionDTO) {
		Question question = new Question();
		CapUser capUser = userRepo.findById(capQuestionDTO.getUserId()).orElseThrow(() -> new NotFound("User Not Found"));
		question.setQuestion(capQuestionDTO.getQuestion());
		question.setTopic(capQuestionDTO.getTopic());
		question.setUser(capUser);
		questionRepo.save(question);
		List<CapAdmin> capAdmins = capAdminRepository.findAll();
		for (CapAdmin capAdmin : capAdmins)
		{
			sendMail(capAdmin.getEmail(), "Question Added by "+capUser.getName());
		}
		return question;
	}

	@Override
	public Answer giveAnswer(@Valid CapAnswerDTO capAnswerDTO) {
		Answer answer = new Answer();
		CapUser answerUser = userRepo.findById(capAnswerDTO.getUserId())
				.orElseThrow(() -> new NotFound("User Not Found"));

		Question question = questionRepo.findById(capAnswerDTO.getQuestionId())
				.orElseThrow(() -> new NotFound("Question Not Found"));
		answer.setQuestion(question);
		answer.setAnswer(capAnswerDTO.getAnswer());
		answer.setAnswerUser(answerUser);

		answerRepo.save(answer);
		
		List<CapAdmin> capAdmins = capAdminRepository.findAll();
		for (CapAdmin capAdmin : capAdmins)
		{
			sendMail(capAdmin.getEmail(), "Answer Added by "+answerUser.getName());
		}
		
		return answer;
	}

	@Override
	public List<Answer> getAnswers(Long questionId) {
		return answerRepo.findByQuestionId(questionId);
	}

	@Override
	public List<Question> getQuestions(String topic) {
		if (topic.equalsIgnoreCase("All")) {
			return questionRepo.findByIsApprovedTrue();
		}
		return questionRepo.findByTopicAndApproved(topic);
	}

	@Override
	public List<Question> searchQuestion(String question) {
		String sqlQuery = "from Question where (question like :question) and isApproved = 1";
		return entityManager.createQuery(sqlQuery, Question.class).setParameter("question", "%" + question + "%")
				.getResultList();
	}

	@Override
	public BodyBuilder uplaodImage(MultipartFile file) throws IOException {
		System.out.println("Original Image Byte Size - " + file.getBytes().length);
		CapImage img = new CapImage(file.getOriginalFilename(), file.getContentType(),
				compressBytes(file.getBytes()));
		imageModelRepo.save(img);
		return ResponseEntity.status(HttpStatus.OK);
	}

	@Override
	public CapImage getImage(String imageName) {
		final Optional<CapImage> retrievedImage = imageModelRepo.findByName(imageName);
		if(retrievedImage.isPresent())
		{
			CapImage img = new CapImage(retrievedImage.get().getName(), retrievedImage.get().getType(), decompressBytes(retrievedImage.get().getPicByte()));
			return img;
		}
		return null;
	}

	@Override
	public CapMessageDTO sendMessage(@Valid CapMessageDTO capMessageDTO) {
		String url = "http://localhost:9595/chat/sendMessage";
		ResponseEntity<CapMessageDTO> responseEntity = restTemplate.postForEntity(url, capMessageDTO, CapMessageDTO.class);
		CapMessageDTO response = responseEntity.getBody();

		return response;
	}

	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();
	}

	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
	public Boolean sendMail(String emailId, String type)
	{
		try
		{
			capEmailService.sendEmail(emailId, type, type);
			return true;
		}
		catch(Exception e)
		{
			System.out.println("error in sending mail " + e);
			return false;
		}
	}
}
