package com.example.demo.CapServiceImpl;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.CapEntity.CapAdmin;
import com.example.demo.CapEntity.Answer;
import com.example.demo.CapEntity.Question;
import com.example.demo.CapModelDTO.AlreadyThere;
import com.example.demo.CapModelDTO.CapResponseDTO;
import com.example.demo.CapModelDTO.NotFound;
import com.example.demo.CapRepository.CapAdminRepository;
import com.example.demo.CapRepository.CapAnswerRepository;
import com.example.demo.CapRepository.CapQuestionRepository;
import com.example.demo.CapRepository.CapUserRepository;
import com.example.demo.CapService.CapAdminService;
import com.example.demo.CapService.CapEmailService;
import com.example.demo.CapEntity.CapUser;

@Service
public class CapAdminServiceImpl implements CapAdminService
{
	@Autowired
	private CapAdminRepository capAdminRepository;
	@Autowired
	private CapUserRepository userRepo;
	@Autowired
	private CapQuestionRepository capQuestionRepository;
	@Autowired
	private CapAnswerRepository capAnswerRepository;

	@Override
	public CapAdmin adminLogin(String email, String password)
	{
		CapAdmin capAdmin = capAdminRepository.findByEmail(email);
		if (Objects.isNull(capAdmin))
			throw new NotFound();

		if (capAdmin.getPassword().equals(password))
		{
			capAdmin.setIsActive(true);
			capAdminRepository.save(capAdmin);
		}
		else
			throw new NotFound();
		return capAdmin;
	}

	@Override
	public String adminLogout(Long adminId)
	{
		CapAdmin capAdmin = capAdminRepository.findById(adminId).orElseThrow(() -> new NotFound("Admin not found"));
		capAdmin.setIsActive(false);
		capAdminRepository.save(capAdmin);
		return "Logged Out";
	}

	@Override
	public CapAdmin adminRegister(CapAdmin capAdmin)
	{
		CapAdmin admin1 = capAdminRepository.findByEmail(capAdmin.getEmail());
		if (Objects.isNull(admin1))
			return capAdminRepository.save(capAdmin);

		throw new AlreadyThere();
	}

	@Override
	public CapUser getUser(String email)
	{
		return userRepo.findByEmail(email);
	}

	@Override
	public List<CapUser> getAllUser()
	{
		return userRepo.findAll();
	}

	@Override
	public List<Question> getUnApprovedQuestions()
	{
		return capQuestionRepository.findByIsApproved();
	}

	@Override
	public List<Answer> getUnApprovedAnswers()
	{
		return capAnswerRepository.findByIsApproved();
	}

	@Override
	public Question approveQuestion(Long questionId)
	{
		Question question = capQuestionRepository.findById(questionId).orElseThrow(() -> new NotFound("Question not found"));

		question.setIsApproved(true);
		question = capQuestionRepository.save(question);
		return question;
	}

	@Override
	public Answer approveAnswer(Long answerId)
	{
		Answer answer = capAnswerRepository.findById(answerId).orElseThrow(() -> new NotFound("Answer not found"));

		answer.setIsApproved(true);
		answer = capAnswerRepository.save(answer);
		return answer;
	}

	@Override
	public CapResponseDTO deleteQuestion(Long questionId)
	{
		CapResponseDTO capResponseDTO = new CapResponseDTO();
		Question question = capQuestionRepository.findById(questionId).orElseThrow(() -> new NotFound("Question not found"));

		capQuestionRepository.delete(question);
		capResponseDTO.setMsg("Question removed");
		return capResponseDTO;
	}

	@Override
	public CapResponseDTO deleteAnswer(Long answerId)
	{
		CapResponseDTO capResponseDTO = new CapResponseDTO();

		Answer answer = capAnswerRepository.findById(answerId).orElseThrow(() -> new NotFound("Answer not found"));

		capAnswerRepository.delete(answer);
		capResponseDTO.setMsg("Answer Removed");
		return capResponseDTO;
	}
}
