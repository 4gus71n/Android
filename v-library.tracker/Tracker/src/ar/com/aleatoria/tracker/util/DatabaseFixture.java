package ar.com.aleatoria.tracker.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import ar.com.aleatoria.tracker.db.StudyTimeDAO;
import ar.com.aleatoria.tracker.db.Tags;
import ar.com.aleatoria.tracker.db.model.StudyTime;

public class DatabaseFixture {

	private static String[] _tags = {
		"Banana", "Gaznate", "Pichiruchi",
		"Botarate", "Pelagatos", "Sodoma",
		"Gomorra", "Giambattista Grignani",
		"Pedro el Escamoso"
	};
	// This method populate the database, but be careful because this methos
	// writes really-long study times in the db
	public static int random(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public static void populateDatabase() throws NumberFormatException,
			ParseException {
		StudyTimeDAO dao = ApplicationContext.getInstance().getStudyTimeDAO();
		if (dao.getAll().size() > 0)
			return;

		Calendar[] dates_a = new Calendar[7];
		Calendar[] dates_b = new Calendar[7];

		for (int i = 0; i < 7; i++) {
			dates_a[i] = Calendar.getInstance();
			int dayOfMonth = dates_a[i].get(Calendar.DAY_OF_MONTH);
			dates_a[i].set(Calendar.DAY_OF_MONTH, dayOfMonth - i);

			dates_b[i] = Calendar.getInstance();
			dates_b[i].set(Calendar.DAY_OF_MONTH, dayOfMonth - i);
			dates_b[i].set(Calendar.HOUR_OF_DAY,
					dates_b[i].get(Calendar.HOUR_OF_DAY) + random(1, 3));
			dates_b[i].set(Calendar.MINUTE, dates_b[i].get(Calendar.MINUTE)
					+ random(0, 59));
		}

		for (int i = 0; i < 7; i++) {
			StudyTime studyTime = new StudyTime(dates_a[i].getTime(),
					dates_b[i].getTime());
			studyTime.id = dao.add(studyTime);
			applyTags(studyTime);
		}
	}

	private static void applyTags(StudyTime studyTime) {
		int numberOfTags = random(1, 9);
		ArrayList<String> tagsToApply = new ArrayList<String>();
		for (int i = 0; i < numberOfTags; i++)
			tagsToApply.add(_tags[random(0, 8)]);
		Tags tags = new Tags();
		tags.applyTags(studyTime, tagsToApply);
	}
}
