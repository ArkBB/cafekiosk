package sample.cafekiosk.spring.domain.hisotry.mail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailSendHistoryRepository extends JpaRepository<MailSendHistory,Long> {
}
