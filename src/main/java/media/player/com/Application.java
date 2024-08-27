package media.player.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/*@Override
	public void run(String... args) {
		BlockHound.builder().with(this).install();
		//BlockHound.install();
	}

	@Override
	public void applyTo(BlockHound.Builder builder) {
		builder.allowBlockingCallsInside(
				"java.io.RandomAccessFile",
				"readBytes"
		);
		builder.allowBlockingCallsInside(
				"java.io.FileInputStream",
				"readBytes"
		);
	}*/
}
