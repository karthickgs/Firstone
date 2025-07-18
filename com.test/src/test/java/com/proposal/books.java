package com.proposal;

public class books {
	
	int quantity;
	
	public void setbook(int quan) {
		if(quan>0)
		quantity = quan;
	}
	
	public int getbook() {
		return quantity;
	}
	
	public void profit(int pro) {
		setbook(quantity+pro);
	}
	
}
