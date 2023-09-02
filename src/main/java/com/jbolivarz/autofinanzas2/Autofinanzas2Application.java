package com.jbolivarz.autofinanzas2;

import com.jbolivarz.autofinanzas2.models.Cuenta;
import com.jbolivarz.autofinanzas2.models.Transaccion;
import com.jbolivarz.autofinanzas2.models.Usuario;
import com.jbolivarz.autofinanzas2.services.CuentaProducerService;
import com.jbolivarz.autofinanzas2.services.TransaccionProducerService;
import com.jbolivarz.autofinanzas2.services.UsuarioProducerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class Autofinanzas2Application implements CommandLineRunner {

	private final UsuarioProducerService usuarioProducerService;
	private final CuentaProducerService cuentaProducerService;
	private final TransaccionProducerService transaccionProducerService;

	public Autofinanzas2Application(UsuarioProducerService usuarioProducerService, CuentaProducerService cuentaProducerService, TransaccionProducerService transaccionProducerService) {
		this.usuarioProducerService = usuarioProducerService;
		this.cuentaProducerService = cuentaProducerService;
		this.transaccionProducerService = transaccionProducerService;
	}

	public static void main(String[] args) {
		SpringApplication.run(Autofinanzas2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		producerData();
	}

	private void producerData(){
		String topico = "Usuario-2023-9";
		Usuario usuario1 = new Usuario(11, "23456789", "Andres", "Perez", "andres.perez@correo.com", "3045678954");
		Usuario usuario2 = new Usuario(12, "89765234", "Andres", "Perez", "andres.perez@correo.com", "3045678954");
		usuarioProducerService.sendKey(topico, usuario1.getId(), usuario1);
		usuarioProducerService.sendKey(topico, usuario2.getId(), usuario2);

		String topicoCuenta = "Cuenta-2023-9";
		Cuenta cuenta1 = new Cuenta(11, 11, "DR67167257123765", "Cuenta de ahorros", 85000D);
		Cuenta cuenta2= new Cuenta(12, 12, "TY7657652763113", "Cuenta de ahorros", 56000D);
		cuentaProducerService.sendKey(topicoCuenta, cuenta1.getId(), cuenta1);
		cuentaProducerService.sendKey(topicoCuenta, cuenta2.getId(), cuenta2);

		String topicoTransaccion = "Transaccion-2023-9";
		Transaccion transaccion1 = new Transaccion(8, 11, 10, LocalDate.parse("2023-09-02"), "Pago de sueldo", 10000D);
		Transaccion transaccion2 = new Transaccion(8, 12, 10, LocalDate.parse("2023-09-02"), "Pago de sueldo", 30000D);
		transaccionProducerService.sendKey(topicoTransaccion, transaccion1.getId(), transaccion1);
		transaccionProducerService.sendKey(topicoTransaccion, transaccion2.getId(), transaccion2);

	}
}
