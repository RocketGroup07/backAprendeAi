package br.com.aprendeai.util;

import java.util.concurrent.ThreadLocalRandom;

public class OtpGenerator {

	public static String generateOtp() {
        int randomNum = ThreadLocalRandom.current().nextInt(10000000, 100000000); // gera um 'token' de 8 digitos numericos para a redefinicao de senha
        
        return String.valueOf(randomNum);
    }
}
