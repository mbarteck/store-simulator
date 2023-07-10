package com.mbartecki.storesimulator.port;

import com.mbartecki.storesimulator.dto.EmailDto;

public interface EmailSenderPort {


  void sendEmail(EmailDto emailDto);
}
