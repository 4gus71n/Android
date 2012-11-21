package ar.com.aleatoria.tracker.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ar.com.aleatoria.tracker.db.model.StudyTime;
import ar.com.aleatoria.tracker.db.model.Tag;
import ar.com.aleatoria.tracker.db.model.TimeTag;
import ar.com.aleatoria.tracker.util.ApplicationContext;

public class Tags {
	public TagDAO tagDao = ApplicationContext.getInstance().getTagDAO();
	public TimeTagDAO timeTagDao = ApplicationContext.getInstance().getTimeTagDAO();
	public Tags() {
	}

	public void applyTags(StudyTime studyTime, CharSequence tags) {
		applyTags(studyTime, parse(tags.toString()));
	}
	
	public void applyTags(StudyTime studyTime, List<String> tagsToApply) {
		TagDAO dao = ApplicationContext.getInstance().getTagDAO();
		
		// 1. Save Tag objects, to reference later from TimeTag
		ArrayList<Tag> _tags = new ArrayList<Tag>();
		
		for (String tag: tagsToApply) {
			Tag _realTag = new Tag();
			_realTag.id = dao.getIdFrom(tag);
			_realTag.tag = tag; // I swear I'm not stupid
			_tags.add(_realTag);
		}
		tagDao.persist(_tags);
		// 2. Save TimeTag objects referencing the study time
		for (Tag tag: _tags) {
			TimeTag timeTag = new TimeTag();
			timeTag.tag = tag.id;
			timeTag.time = studyTime.id;
			timeTagDao.add(timeTag);
		}
		ApplicationContext.getInstance().refreshAutocompleteTagList();
	}
	
	public List<String> parse(String input) {
		List<String> tags = Arrays.asList(input.split("\\s*,\\s*"));
		return tags;
	}
	
	public String commaSeparate(List<Tag> tags) {
		String list = "";
		for (Tag t: tags)
			if (t.tag.length() > 0)
				list += t.tag + ", ";
		if (list.length() > 0)
			list = list.substring(0, list.length()-2);
		return list;
	}
	
	public List<Tag> getTagsForStudyTime(StudyTime study) {
		TimeTagDAO timeTagDao = ApplicationContext.getInstance().getTimeTagDAO();
		TagDAO tagDao = ApplicationContext.getInstance().getTagDAO();
		List<TimeTag> time_tags = timeTagDao.getAll("time = " + study.id);
		String where=""; 
		for (TimeTag tag: time_tags) {
			where+=","+tag.getId();
		}
		where = where.substring(1, where.length());
		return tagDao.getAll("id in ("+where+")");
	}
	
	public List<StudyTime> getStudyTimesForTags(List<Tag> tags) {
		TimeTagDAO timeTagDao = ApplicationContext.getInstance().getTimeTagDAO();
		StudyTimeDAO studyTimeDao = ApplicationContext.getInstance().getStudyTimeDAO();
		//Put all tags id in the same string to use it in a where
		String where=""; 
		for (Tag tag: tags) {
			where+=","+tag.getId();
		}
		where = where.substring(1, where.length());
		//Use that string in a query to get all middle-relationship rows (TimeTags)
		List<TimeTag> timetags = timeTagDao.getAll(timeTagDao.getTableName()+".id in ("+where+") ");
		//Once more use that id to get, finally, the StudyTimes ids
		where = "";
		for (TimeTag ttag : timetags) {
			where+=","+ttag.getId();
		}
		where = where.substring(1, where.length());
		return studyTimeDao.getAll(studyTimeDao.getTableName()+".id = ("+where+")");
	}
}
