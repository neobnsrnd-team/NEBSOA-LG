package test.email;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

public class MailTest {

    public static void main(String[] args) throws Exception {
//      Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath("D:/KEB-진척율 산정자료-상세일정계획(프레임웍).xls");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("첨부파일");
        attachment.setName(new String("KEB-진척율 산정자료-상세일정계획(프레임웍).xls".getBytes("euc-kr"), "latin1"));

//      Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setCharset("euc-kr");
        email.setHostName("mail.serverside.co.kr");
        email.addTo("kyun@serverside.co.kr", "kyun");
        email.setFrom("kyun@serverside.co.kr", "kyun");
        email.setSubject("첨부파일 테스트");
        email.setMsg("첨부파일 테스트");

//      add the attachment
        email.attach(attachment);

//      send the email
//      email.setAuthentication(id, passwd);
        email.send();
    }
}
