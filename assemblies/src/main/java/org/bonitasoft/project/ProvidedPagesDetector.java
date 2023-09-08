/** 
 * Copyright (C) 2023 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import javax.xml.bind.JAXBException;

import org.bonitasoft.engine.business.application.exporter.ApplicationNodeContainerConverter;
import org.bonitasoft.engine.business.application.xml.ApplicationNodeContainer;
import org.bonitasoft.engine.business.application.xml.ApplicationPageNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class ProvidedPagesDetector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProvidedPagesDetector.class);

    private ApplicationNodeContainerConverter converter = new ApplicationNodeContainerConverter();

    private static final Set<String> PROVIDED_PAGES = Set.of("custompage_tasklist",
            "custompage_processlistBonita",
            "custompage_userCaseListBonita",
            "custompage_userCaseDetailsBonita");

    public boolean installProvidedPages(File applicationsFolder) {
        var xmlFiles = applicationsFolder.listFiles(f -> f.getName().endsWith(".xml"));
        if (xmlFiles != null) {
            LOGGER.info("Detecting provided pages usage in applications...");
            var installProvidedPages = Stream.of(xmlFiles)
                    .map(this::toApplicationContainerNode)
                    .filter(Objects::nonNull)
                    .flatMap(ProvidedPagesDetector::listPages)
                    .anyMatch(PROVIDED_PAGES::contains);
            LOGGER.info(installProvidedPages ? "INSTALL_PROVIDED_PAGES enabled." : "INSTALL_PROVIDED_PAGES disabled.");
            return installProvidedPages;
        }
        return false;
    }

    private ApplicationNodeContainer toApplicationContainerNode(File file) {
        try {
            return converter.unmarshallFromXML(Files.readAllBytes(file.toPath()));
        } catch (JAXBException | IOException | SAXException e) {
            LOGGER.warn("Cannot parse {}. File skipped from provided page detection.", file.getAbsolutePath());
            return null;
        }
    }

    private static Stream<String> listPages(ApplicationNodeContainer applicationNode) {
        return applicationNode.getApplications().stream()
                .flatMap(application -> application.getApplicationPages().stream()
                        .map(ApplicationPageNode::getCustomPage));
    }

}
