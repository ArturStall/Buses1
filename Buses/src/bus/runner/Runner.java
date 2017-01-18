package bus.runner;

import bus.init.Initialization;

public class Runner {

	public static void main(String[] args) {
		String fileConfig = "src/config.properties";
		runProgramm(fileConfig);
	}
	
	private static void runProgramm(String fileConfig) {
		String config = fileConfig;
		Initialization init = new Initialization();
		init.setFileName(config);
		init.readParam();
		init.createModel();
		init.startMotion();
	}
}