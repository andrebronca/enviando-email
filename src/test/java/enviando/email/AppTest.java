package enviando.email;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
//	private String userName = "arb.teste.envio@gmail.com";
//	private String senha = "AaBbCcXxYyZz@22";

	@Test
	public void testeEmail() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("Oi,<br/><br/>");
		sb.append("<h2>Recebendo um e-mail do curso de java</h2>");
		sb.append("<h3>Email no formato html</h3>");
		sb.append("Curso<br/>");
		sb.append("<a target=\"_blank\" href=\"http://projetojavaweb.com/certificado-aluno/login\" style=\"color : #2525a7; padding : 14px 25px; text-align : center; text-decoration : none; display : inline-block; border-radius : 10px; font-size : 20px; font-family : courier; border : 3px solid green; background-color : #99da39;\" >Acessar o portal</a><br/>");
		sb.append("<hr/>");
		
		
		ObjetoEnviaEmail oee = new ObjetoEnviaEmail("arb.teste.envio@gmail.com", "Andre Java Curso", "Envio de e-mail",
				sb.toString());
//		oee.enviarEmail(false);
		oee.enviarEmail(true);

	}
}
