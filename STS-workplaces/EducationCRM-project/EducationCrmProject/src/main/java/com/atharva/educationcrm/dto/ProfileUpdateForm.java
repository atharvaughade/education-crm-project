package com.atharva.educationcrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProfileUpdateForm {

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10 digit mobile number")
    private String mobile;

    @NotBlank(message = "City is required")
    @Size(max = 50)
    private String city;

    public String getFirstName() { 
    	return firstName; 
    }
    public void setFirstName(String firstName) { 
    	this.firstName = firstName; 
    }
    
    public String getLastName() { 
    	return 
    			lastName; 
    }
    public void setLastName(String lastName) { 
    	this.lastName = lastName;
    }
    
    public String getMobile() { 
    	return mobile; 
    }
    public void setMobile(String mobile) { 
    	this.mobile = mobile; 
    }
    
    public String getCity() { 
    	return city; 
    }
    public void setCity(String city) { 
    	this.city = city; 
    }
}
