package com.example.demo.CapService;

import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.CapEntity.Answer;
import com.example.demo.CapEntity.CapImage;
import com.example.demo.CapEntity.Question;
import com.example.demo.CapModelDTO.CapAnswerDTO;
import com.example.demo.CapModelDTO.CapQuestionDTO;
import com.example.demo.CapModelDTO.CapMessageDTO;
import com.example.demo.CapModelDTO.CapResponseDTO;
import com.example.demo.CapEntity.CapUser;

public interface CapUserService
{
	public CapUser userLogin(String email, String password);
	public String userLogout(Long userId);
	public CapUser userRegister(@Valid CapUser capUser);
	
	public Question askQuestion(@Valid CapQuestionDTO capQuestionDTO);
	public Answer giveAnswer(@Valid CapAnswerDTO capAnswerDTO);
	
	
	public List<Question> searchQuestion(String question);
	
	public List<Answer> getAnswers(Long questionId);
	public List<Question> getQuestions(String topic);
	
	public BodyBuilder uplaodImage(MultipartFile file) throws IOException;
	public CapImage getImage(String imageName);
	
	public CapMessageDTO sendMessage(@Valid CapMessageDTO capMessageDTO);
}
