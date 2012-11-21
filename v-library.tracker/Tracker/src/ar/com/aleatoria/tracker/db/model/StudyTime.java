package ar.com.aleatoria.tracker.db.model;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import ar.com.aleatoria.tracker.db.StudyTimeDAO;
import ar.com.aleatoria.tracker.db.model.Model;
import ar.com.aleatoria.tracker.util.ApplicationContext;
import ar.com.aleatoria.tracker.util.ISO8601;

/**
 * @author Agus
 * This class shows the time that a person has dedicated to studying. 
 */
public class StudyTime extends Model {
	private Date startedTime;
	private Date finishedTime;
		
	public StudyTime(Date startedTime, Date finishedTime) {
		this.startedTime = startedTime;
		this.finishedTime = finishedTime;
	}

	public StudyTime() {
		// TODO Auto-generated constructor stub
	}

	public Date getStartedTime() {
		return startedTime;
	}
	public void setStartedTime(Date startedTime) {
		this.startedTime = startedTime;
	}
	public Date getFinishedTime() {
		return finishedTime;
	}
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}

	private static Long diffBetweenTimes(Date startedTime2, Date finishedTime2) {
		return (finishedTime2.getTime() - startedTime2.getTime())/60000;
	}

	/**
	 * @param weekend A set of days that we use to retrive their respective StudyTimes from the db
	 * @return A HashMap with String as key that represents the date of that StudyTimes, and of course
	 * 		   the values are the total amount of time of all StudyTimes for that day
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	public static LinkedHashMap<String, Long> totalTimeStudiedInDays(List<Date> weekend) throws NumberFormatException, ParseException {
		LinkedHashMap<String,Long> result = new LinkedHashMap<String, Long>();
		StudyTimeDAO db = ApplicationContext.getInstance().getStudyTimeDAO();
		//We take one day of that weekend
		for (Date weekendDay : weekend) {
			//We sum all StudyTimes of that day and save it into the hash
			Long totalTime = (long) 0;
			for (StudyTime studyOfOneDay : db.getStudyTimesOfTheDate(weekendDay) ) {
				totalTime += diffBetweenTimes(studyOfOneDay.getStartedTime(), studyOfOneDay.getFinishedTime());
			}
			String key = ISO8601.formatTinyDate(weekendDay);
			result.put(key, totalTime);
		}
		return result;
	}

	public long getTotalTime() {
		return diffBetweenTimes(startedTime,finishedTime);
	}
	
}
