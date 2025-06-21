package sample.cafekiosk.spring.util.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.hisotry.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.hisotry.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given

        when(mailSendClient.sendEmail(anyString(),anyString(),anyString(),anyString()))
                .thenReturn(true);
//      when(mailSendHistoryRepository.save(any(MailSendHistory.class)))
//                .thenReturn(true);
//      다 stubbing을 하지 않아도 Mockito 안 구현체 public static final Answer<Object> RETURNS_DEFAULTS = Answers.RETURNS_DEFAULTS
//      에 의해 mailSendHistoryRepository 구현체는 null을 반환한다.
//      @Spy 사용시 나머지 일부만 stubbing 하고 싶을 때.
//        doReturn(true)
//                .when(mailSendClient)
//                .sendEmail(anyString(),anyString(),anyString(),anyString());

        // when
        boolean result = mailService.sendMail("from", "to", "subject", "content");

        // then
        Mockito.verify(mailSendHistoryRepository, Mockito.times(1)).save(any(MailSendHistory.class));

        assertThat(result).isTrue();

    }
}