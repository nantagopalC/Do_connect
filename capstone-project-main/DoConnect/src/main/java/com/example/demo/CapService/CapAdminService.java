package com.example.demo.CapService;

import java.util.List;

import com.example.demo.CapEntity.CapAdmin;
import com.example.demo.CapEntity.Answer;
import com.example.demo.CapEntity.Question;
import com.example.demo.CapModelDTO.CapResponseDTO;
import com.example.demo.CapEntity.CapUser;

public interface CapAdminService
{
	public CapAdmin adminLogin(String email, String password);
	public String adminLogout(Long adminId);
	public CapAdmin adminRegister(CapAdmin capAdmin);
	
	public Question approveQuestion(Long questionId);
	public Answer approveAnswer(Long answerId);
	
	public List<Question> getUnApprovedQuestions();
	public List<Answer> getUnApprovedAnswers();
	
	public CapResponseDTO deleteQuestion(Long questionId);
	public CapResponseDTO deleteAnswer(Long answerId);
	
	public CapUser getUser(String email);
	public List<CapUser> getAllUser();
}
