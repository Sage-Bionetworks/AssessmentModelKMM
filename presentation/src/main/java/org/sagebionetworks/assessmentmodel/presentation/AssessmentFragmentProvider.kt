package org.sagebionetworks.assessmentmodel.presentation

import org.sagebionetworks.assessmentmodel.BranchNode

interface AssessmentFragmentProvider {

    fun fragmentFor(branchNode: BranchNode): AssessmentFragment

}