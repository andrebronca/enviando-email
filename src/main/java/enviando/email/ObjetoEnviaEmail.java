package enviando.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ObjetoEnviaEmail {
	private String userName = "arb.teste.envio@gmail.com";
	private String senha = "AaBbCcXxYyZz@22";
	private String listaDestinatarios = "";
	private String nomeRemetente = "";
	private String assuntoEmail = "";
	private String textoEmail = "";

	public ObjetoEnviaEmail(String listaDestinatarios, String nomeRemetente, String assuntoEmail, String textoEmail) {
		this.listaDestinatarios = listaDestinatarios; // a@a.com,b@a.com,c@a.com (separados por vírgula)
		this.nomeRemetente = nomeRemetente;
		this.assuntoEmail = assuntoEmail;
		this.textoEmail = textoEmail;
	}

	public void enviarEmail(boolean envioHtml) throws Exception {
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, senha);
			}
		});

		Address[] toUsers = InternetAddress.parse(listaDestinatarios); // pode ser uma lista: "a@a.com,b@a.com,c@a.com"
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName, nomeRemetente)); // quem está enviando
		message.setRecipients(Message.RecipientType.TO, toUsers); // email de destino
		message.setSubject(assuntoEmail); // assunto do e-mail

		// mudar a estrutura para envio com anexo, essa parte seria pra envio simles de
		// e-mails
//		if (envioHtml) {
//			message.setContent(textoEmail, "text/html; charset=utf-8");
//		} else {
//			message.setText(textoEmail); // texto do e-mail
//		}

		MimeBodyPart corpoEmail = new MimeBodyPart();
		if (envioHtml) {
			corpoEmail.setContent(textoEmail, "text/html; charset=utf-8");
		} else {
			corpoEmail.setText(textoEmail); // texto do e-mail
		}

		// simulando uma lista de pdf anexado
		List<FileInputStream> pdfs = new ArrayList<>();
		pdfs.add(simuladorDePdf());
		pdfs.add(simuladorDePdf());
		pdfs.add(simuladorDePdf());

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(corpoEmail);

		int idPdf = 1;
		for (FileInputStream arqPdf : pdfs) {

			MimeBodyPart anexoEmail = new MimeBodyPart();
			anexoEmail.setDataHandler(new DataHandler(new ByteArrayDataSource(arqPdf, "application/pdf")));
			anexoEmail.setFileName("anexoemail_"+ (idPdf++) +".pdf");

			multipart.addBodyPart(anexoEmail);
		}

		message.setContent(multipart);

		Transport.send(message);
	}

	/**
	 * simula um pdf ou qualquer arquivo que possa ser enviado por e-mail. Arquivo
	 * de bd base64, bite[], stream, diretório Retorna um PDF em branco com o texto
	 * do paragrafo de exemplo
	 */
	private FileInputStream simuladorDePdf() throws Exception {
		Document document = new Document();
		File file = new File("pdf_anexo.pdf");
		file.createNewFile();
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.add(new Paragraph("Conteúdo do Pdf anexo gerado com Java Mail."));
		document.close();
		return new FileInputStream(file);
	}
}
