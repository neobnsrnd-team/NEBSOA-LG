package test.schedule;

import nebsoa.common.log.LogManager;
import nebsoa.common.schedule.SimpleJob;

public class ScheduleSample  extends SimpleJob{

	public static int scheduleCnt = 0;
	public void execute() {

		scheduleCnt++;
		LogManager.debug("§§§§§§§§§스케쥴러 테스트중 §§§§§§§§ scheduleCnt = "+scheduleCnt);
	}

}
