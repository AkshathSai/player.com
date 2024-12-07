package media.player.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class Application {

	/*static {
		BlockHound.install();
	}*/

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
