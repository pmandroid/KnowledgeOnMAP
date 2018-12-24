package com.vfig.konm.knowledgeonmap.parser.beans.prototypebean;

import java.util.List;

public class Terms{
	private String description;

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	@Override
 	public String toString(){
		return 
			"Terms{" + 
			"description = '" + description + '\'' + 
			"}";
		}
}