/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_VALUE_DATE_SUPPORTED;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input" where type is "week".
 *
 * @author Ahmed Ashour
 */
public class HtmlWeekInput extends HtmlInput {

    private static DateTimeFormatter FORMATTER_ = DateTimeFormatter.ofPattern("yyyy-'W'ww");

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlWeekInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueAttribute(final String newValue) {
        try {
            if (hasFeature(JS_INPUT_SET_VALUE_DATE_SUPPORTED)) {
                FORMATTER_.parse(newValue);
            }
            super.setValueAttribute(newValue);
        }
        catch (final DateTimeParseException e) {
            // ignore
        }
    }
}
