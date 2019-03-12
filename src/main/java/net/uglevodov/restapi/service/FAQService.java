/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.service;

import net.uglevodov.restapi.entities.FAQEntry;

import java.util.List;

public interface FAQService extends GenericService<FAQEntry> {
    List<FAQEntry> findAllByCategory(Integer category);
    List<FAQEntry> findAllByQuestionOrAnswerContaining(String string);

}
