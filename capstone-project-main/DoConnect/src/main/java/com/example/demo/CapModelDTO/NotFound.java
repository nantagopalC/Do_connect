package com.example.demo.CapModelDTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class NotFound extends RuntimeException
{
	public NotFound() {
		
	}
	public NotFound(String errorMsg) {
		super();
		this.errorMsg=errorMsg;
	}
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private String errorMsg;

}
