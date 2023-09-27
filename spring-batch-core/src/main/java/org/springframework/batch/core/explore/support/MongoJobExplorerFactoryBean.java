package org.springframework.batch.core.explore.support;

import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.MongoExecutionContextDao;
import org.springframework.batch.core.repository.dao.MongoJobExecutionDao;
import org.springframework.batch.core.repository.dao.MongoJobInstanceDao;
import org.springframework.batch.core.repository.dao.MongoStepExecutionDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

public class MongoJobExplorerFactoryBean extends AbstractJobExplorerFactoryBean implements InitializingBean {

    private MongoOperations mongoOperations;

    public void setMongoOperations(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    protected JobInstanceDao createJobInstanceDao() {
        return new MongoJobInstanceDao(this.mongoOperations);
    }

    @Override
    protected JobExecutionDao createJobExecutionDao() {
        return new MongoJobExecutionDao(this.mongoOperations);
    }

    @Override
    protected StepExecutionDao createStepExecutionDao() {
        return new MongoStepExecutionDao(this.mongoOperations);
    }

    @Override
    protected ExecutionContextDao createExecutionContextDao() {
        return new MongoExecutionContextDao(this.mongoOperations);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(this.mongoOperations, "MongoOperations must not be null.");
    }
}
