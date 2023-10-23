package org.springframework.batch.core.repository.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

// Based on https://www.mongodb.com/blog/post/generating-globally-unique-identifiers-for-use-with-mongodb
// Section: Use a single counter document to generate unique identifiers one at a time
public class MongoSequenceIncrementer implements DataFieldMaxValueIncrementer {

	private final MongoOperations mongoTemplate;

	private final String sequenceName;

	public MongoSequenceIncrementer(MongoOperations mongoTemplate, String sequenceName) {
		this.mongoTemplate = mongoTemplate;
		this.sequenceName = sequenceName;
	}

	@Override
	public long nextLongValue() throws DataAccessException {
		// TODO optimize
		MongoSequence sequence = mongoTemplate.findOne(new Query(), MongoSequence.class, sequenceName);
		Query query = query(where("_id").is(sequence.getId()));
		Update update = new Update().inc("count", 1);
		// The following does not return the modified document
		mongoTemplate.findAndModify(query, update, MongoSequence.class, sequenceName);
		return mongoTemplate.findOne(new Query(), MongoSequence.class, sequenceName).getCount();
	}

	@Override
	public int nextIntValue() throws DataAccessException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String nextStringValue() throws DataAccessException {
		throw new UnsupportedOperationException();
	}

	public static final class MongoSequence {

		private String id;

		private long count;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}

		@Override
		public String toString() {
			return "MongoSequence{" +
					"id='" + id + '\'' +
					", count=" + count +
					'}';
		}
	}
}
