package org.transmartproject.rest

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder
import org.transmartproject.core.dataquery.Patient
import org.transmartproject.core.dataquery.TabularResult
import org.transmartproject.core.dataquery.clinical.ClinicalDataResource
import org.transmartproject.core.dataquery.clinical.PatientRow
import org.transmartproject.core.dataquery.clinical.PatientsResource
import org.transmartproject.core.exceptions.InvalidArgumentsException
import org.transmartproject.core.ontology.OntologyTerm
import org.transmartproject.core.ontology.Study
import org.transmartproject.db.dataquery.clinical.variables.TerminalConceptVariable
import org.transmartproject.db.ontology.ConceptsResourceService

class ObservationController {

    static responseFormats = ['json', 'hal']

    ClinicalDataResource clinicalDataResourceService
    StudyLoadingService studyLoadingServiceProxy
    PatientsResource patientsResourceService
    ConceptsResourceService conceptsResourceService

    /** GET request on /studies/XXX/observations/
     *  This will return the list of observations for study XXX
     */
    def index() {
        def study = studyLoadingServiceProxy.study
        TabularResult<TerminalConceptVariable, PatientRow> observations =
                clinicalDataResourceService.retrieveData(study, null, null)
        try {
            respond observations
        } finally {
            observations.close()
        }

    }

    /** GET request on /studies/XXX/concepts/YYY/observations/
     *  This will return the list of observations for study XXX and concept YYY
     */
    def indexByConcept() {
        TabularResult<TerminalConceptVariable, PatientRow> observations =
                clinicalDataResourceService.retrieveData(study, null, [concept])
        respond observations
    }

    /** GET request on /studies/XXX/subjects/YYY/observations/
     *  This will return the list of observations for study XXX and subject YYY
     */
    def indexBySubject() {
        TabularResult<TerminalConceptVariable, PatientRow> observations =
                clinicalDataResourceService.retrieveData(study, [patient], null)
        respond observations
    }

    Study getStudy() { studyLoadingServiceProxy.study }

    OntologyTerm getConcept() {
        GrailsWebRequest webRequest = RequestContextHolder.currentRequestAttributes()
        Long conceptId = Long.parseLong(webRequest.params.get('conceptId'))
        if (!conceptId) {
            throw new InvalidArgumentsException('Could not find a concept id')
        }
        conceptsResourceService.getByKey(conceptId)
    }

    Patient getPatient() {
        GrailsWebRequest webRequest = RequestContextHolder.currentRequestAttributes()
        Long subjectId = Long.parseLong(webRequest.params.get('subjectId'))
        if (!subjectId) {
            throw new InvalidArgumentsException('Could not find a study id')
        }
        patientsResourceService.getPatientById(subjectId)
    }
}
