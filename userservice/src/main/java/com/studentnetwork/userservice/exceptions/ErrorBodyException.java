package com.studentnetwork.userservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class ErrorBodyException{
    private String message;
}
