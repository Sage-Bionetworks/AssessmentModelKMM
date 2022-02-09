package org.sagebionetworks.assessmentmodel.presentation

import org.sagebionetworks.assessmentmodel.BranchNode

interface AssessmentFragmentProvider {

    fun fragmentFor(branchNode: BranchNode): AssessmentFragment?

}

/**
 * For apps with more than one [AssessmentFragmentProvider], [RootAssessmentFragmentProvider] can be used
 * to wrap them all into one. This is common when when including Assessments from multiple libraries,
 * each of which would have it's own [AssessmentFragmentProvider]
 */
class RootAssessmentFragmentProvider(private val providers: List<AssessmentFragmentProvider>) : AssessmentFragmentProvider {

    override fun fragmentFor(branchNode: BranchNode): AssessmentFragment {
        for (provider in providers) {
            val fragment = provider.fragmentFor(branchNode)
            if (fragment != null) {
                return fragment
            }
        }
        return AssessmentFragment()
    }

}