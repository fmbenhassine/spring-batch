/*
 * Copyright 2006-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.core;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a contribution to a {@link StepExecution}, buffering changes until
 * they can be applied at a chunk boundary.
 *
 * @author Dave Syer
 * @author Mahmoud Ben Hassine
 *
 */
@SuppressWarnings("serial")
public class StepContribution implements Serializable {

	private volatile AtomicInteger readCount = new AtomicInteger(0);

	private volatile AtomicInteger writeCount = new AtomicInteger(0);

	private volatile AtomicInteger filterCount = new AtomicInteger(0);

	private final AtomicInteger parentSkipCount = new AtomicInteger(0);

	private volatile AtomicInteger readSkipCount = new AtomicInteger(0);

	private volatile AtomicInteger writeSkipCount = new AtomicInteger(0);

	private volatile AtomicInteger processSkipCount = new AtomicInteger(0);

	private ExitStatus exitStatus = ExitStatus.EXECUTING;

	private volatile StepExecution stepExecution;

	/**
	 * @param execution {@link StepExecution} the stepExecution used to initialize
	 * {@code skipCount}.
	 */
	public StepContribution(StepExecution execution) {
		this.stepExecution = execution;
		this.parentSkipCount.set(execution.getSkipCount());
	}

	/**
	 * Set the {@link ExitStatus} for this contribution.
	 *
	 * @param status {@link ExitStatus} instance to be used to set the exit status.
	 */
	public void setExitStatus(ExitStatus status) {
		this.exitStatus = status;
	}

	/**
	 * Public getter for the status.
	 *
	 * @return the {@link ExitStatus} for this contribution
	 */
	public ExitStatus getExitStatus() {
		return exitStatus;
	}

	/**
	 * Increment the counter for the number of items processed.
	 *
	 * @param count int amount to increment by.
	 */
	public void incrementFilterCount(int count) {
		filterCount.addAndGet(count);
	}

	/**
	 * Increment the counter for the number of items read.
	 */
	public void incrementReadCount() {
		readCount.incrementAndGet();
	}

	/**
	 * Increment the counter for the number of items written.
	 *
	 * @param count int amount to increment by.
	 */
	public void incrementWriteCount(int count) {
		writeCount.addAndGet(count);
	}

	/**
	 * Public access to the read counter.
	 *
	 * @return the item counter.
	 */
	public int getReadCount() {
		return readCount.get();
	}

	/**
	 * Public access to the write counter.
	 *
	 * @return the item counter.
	 */
	public int getWriteCount() {
		return writeCount.get();
	}

	/**
	 * Public getter for the filter counter.
	 *
	 * @return the filter counter
	 */
	public int getFilterCount() {
		return filterCount.get();
	}

	/**
	 * @return the sum of skips accumulated in the parent {@link StepExecution}
	 * and this <code>StepContribution</code>.
	 */
	public synchronized int getStepSkipCount() {
		return readSkipCount.get() + writeSkipCount.get() + processSkipCount.get() + parentSkipCount.get();
	}

	/**
	 * @return the number of skips collected in this
	 * <code>StepContribution</code> (not including skips accumulated in the
	 * parent {@link StepExecution}).
	 */
	public synchronized int getSkipCount() {
		return readSkipCount.get() + writeSkipCount.get() + processSkipCount.get();
	}

	/**
	 * Increment the read skip count for this contribution
	 */
	public void incrementReadSkipCount() {
		readSkipCount.incrementAndGet();
	}

	/**
	 * Increment the read skip count for this contribution
	 *
	 * @param count int amount to increment by.
	 */
	public void incrementReadSkipCount(int count) {
		readSkipCount.set(count);
	}

	/**
	 * Increment the write skip count for this contribution
	 */
	public void incrementWriteSkipCount() {
		writeSkipCount.incrementAndGet();
	}

	/**
	 *
	 */
	public void incrementProcessSkipCount() {
		processSkipCount.incrementAndGet();
	}

	/**
	 * @return the read skip count
	 */
	public int getReadSkipCount() {
		return readSkipCount.get();
	}

	/**
	 * @return the write skip count
	 */
	public int getWriteSkipCount() {
		return writeSkipCount.get();
	}

	/**
	 * Public getter for the process skip count.
	 *
	 * @return the process skip count
	 */
	public int getProcessSkipCount() {
		return processSkipCount.get();
	}

	/**
	 * Public getter for the parent step execution of this contribution.
	 * @return parent step execution of this contribution
	 */
	public StepExecution getStepExecution() {
		return stepExecution;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[StepContribution: read=" + readCount.get() + ", written=" + writeCount.get() + ", filtered=" + filterCount.get()
				+ ", readSkips=" + readSkipCount.get() + ", writeSkips=" + writeSkipCount.get() + ", processSkips="
				+ processSkipCount.get() + ", exitStatus=" + exitStatus.getExitCode() + "]";
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StepContribution)) {
			return false;
		}
		StepContribution other = (StepContribution) obj;
		return toString().equals(other.toString());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 11 + toString().hashCode() * 43;
	}

}
