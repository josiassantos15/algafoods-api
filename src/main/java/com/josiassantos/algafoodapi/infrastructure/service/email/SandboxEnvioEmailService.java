package com.josiassantos.algafoodapi.infrastructure.service.email;

import com.josiassantos.algafoodapi.core.email.EmailProperties;
import com.josiassantos.algafoodapi.domain.service.EnvioEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SandboxEnvioEmailService extends SmtpEnvioEmailService {

	@Autowired
	private EmailProperties emailProperties;
	
	@Override
	protected MimeMessage criarMimeMessage(EnvioEmailService.Mensagem mensagem) throws MessagingException {
		MimeMessage mimeMessage = super.criarMimeMessage(mensagem);
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
		helper.setTo(emailProperties.getSandbox().getDestinatario());
		
		return mimeMessage;
	}

}
