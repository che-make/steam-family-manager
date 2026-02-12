package com.metamedicsvr.springtemplate.error.exception;

// Exception to be thrown when the limit of authorized emails is exceeded
public class EmailLimitExceededException extends RuntimeException {
    public EmailLimitExceededException(int maxAuthorizedEmails, int availableSpace, int currentCount, int recordCount) {
        super("The institution has " + currentCount + " authorized emails. You have a limit of " + maxAuthorizedEmails + " predefined emails, you can add only " + availableSpace + " more but you are trying to add " + recordCount + " more.");
    }
}