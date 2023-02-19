package com.example.demo.CapController;

import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.CapEntity.Answer;
import com.example.demo.CapEntity.CapImage;
import com.example.demo.CapEntity.Question;
import com.example.demo.CapModelDTO.CapAnswerDTO;
import com.example.demo.CapModelDTO.CapQuestionDTO;
import com.example.demo.CapService.CapUserService;
import com.example.demo.CapModelDTO.CapMessageDTO;
import com.example.demo.CapEntity.CapUser;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class CapUserController
{
	@Autowired
	private CapUserService capUserService;
	
	@GetMapping("/login/{email}/{password}")
	public CapUser userLogin(@PathVariable String email, @PathVariable String password) {
		return capUserService.userLogin(email, password);
	}

	@GetMapping("/logout/{userId}")
	public String userLogout(@PathVariable Long userId) {
		return capUserService.userLogout(userId);
	}

	@PostMapping("/register")
	public CapUser userRegister(@Valid @RequestBody CapUser capUser) {
		return capUserService.userRegister(capUser);
	}

	@PostMapping("/askQuestion")
	public Question askQuestion(@Valid @RequestBody CapQuestionDTO capQuestionDTO) {
		return capUserService.askQuestion(capQuestionDTO);
	}

	@PostMapping("/giveAnswer")
	public Answer giveAnswer(@Valid @RequestBody CapAnswerDTO capAnswerDTO) {
		return capUserService.giveAnswer(capAnswerDTO);
	}

	@GetMapping("/searchQuestion/{question}")
	public List<Question> searchQuestion(@PathVariable String question) {
		return capUserService.searchQuestion(question);
	}

	@GetMapping("/getAnswers/{questionId}")
	public List<Answer> getAnswers(@PathVariable Long questionId) {
		return capUserService.getAnswers(questionId);
	}

	@GetMapping("/getQuestions/{topic}")
	public List<Question> getQuestions(@PathVariable String topic) {
		return capUserService.getQuestions(topic);
	}

	@PostMapping("/upload")
	public BodyBuilder uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
		return capUserService.uplaodImage(file);
	}

	@GetMapping(path = { "/get/{imageName}" })
	public CapImage getImage(@PathVariable("imageName") String imageName) throws IOException {
		return capUserService.getImage(imageName);
	}

	@PostMapping("/sendMessage")
	public CapMessageDTO sendMessage(@Valid @RequestBody CapMessageDTO capMessageDTO) {
		return capUserService.sendMessage(capMessageDTO);
	}
}
