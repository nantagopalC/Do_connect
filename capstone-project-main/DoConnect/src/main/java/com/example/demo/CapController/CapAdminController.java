package com.example.demo.CapController;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.CapEntity.CapAdmin;
import com.example.demo.CapEntity.Answer;
import com.example.demo.CapEntity.Question;
import com.example.demo.CapModelDTO.CapResponseDTO;
import com.example.demo.CapService.CapAdminService;
import com.example.demo.CapEntity.CapUser;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class CapAdminController
{
	@Autowired
	private CapAdminService capAdminService;

	@GetMapping("/login/{email}/{password}")
	public CapAdmin adminLogin(@PathVariable String email, @PathVariable String password) {
		return capAdminService.adminLogin(email, password);
	}

	@GetMapping("/logout/{adminId}")
	public String adminLogout(@PathVariable Long adminId) {
		return capAdminService.adminLogout(adminId);
	}

	@PostMapping("/register")
	public CapAdmin adminRegister(@Valid @RequestBody CapAdmin capAdmin) {
		return capAdminService.adminRegister(capAdmin);
	}
	
	@GetMapping("/getUnApprovedQuestions")
	public List<Question> getUnApprovedQuestions() {
		return capAdminService.getUnApprovedQuestions();
	}

	@GetMapping("/getUnApprovedAnswers")
	public List<Answer> getUnApprovedAnswers() {
		return capAdminService.getUnApprovedAnswers();
	}

	@PutMapping("/approveQuestion/{questionId}")
	public Question approveQuestion(@PathVariable Long questionId) {
		return capAdminService.approveQuestion(questionId);
	}

	@PutMapping("/approveAnswer/{answerId}")
	public Answer approveAnswer(@PathVariable Long answerId) {
		return capAdminService.approveAnswer(answerId);
	}

	@DeleteMapping("/deleteQuestion/{questionId}")
	public CapResponseDTO deleteQuestion(@PathVariable Long questionId) {
		return capAdminService.deleteQuestion(questionId);
	}

	@DeleteMapping("/deleteAnswer/{answerId}")
	public CapResponseDTO deleteAnswer(@PathVariable Long answerId) {
		return capAdminService.deleteAnswer(answerId);
	}

	@GetMapping("/getUser/{email}")
	public CapUser getUser(@PathVariable String email) {
		return capAdminService.getUser(email);
	}

	@GetMapping("/getAllUsers")
	public List<CapUser> getAllUser() {
		return capAdminService.getAllUser();
	}

}
