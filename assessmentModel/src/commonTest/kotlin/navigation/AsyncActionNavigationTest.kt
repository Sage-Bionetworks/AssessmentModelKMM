package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.PermissionInfo
import org.sagebionetworks.assessmentmodel.PermissionType
import org.sagebionetworks.assessmentmodel.RecorderConfiguration
import org.sagebionetworks.assessmentmodel.serialization.AssessmentObject
import org.sagebionetworks.assessmentmodel.serialization.PermissionInfoObject
import kotlin.test.*

class AsyncActionNavigationTest : NavigationTestHelper() {

    data class TestConfig(
        override val identifier: String,
        override val startStepIdentifier: String? = null,
        override val stopStepIdentifier: String? = null,
        override val permissions: List<PermissionInfo>? = null,
        override val reason: String? = null,
        override val optional: Boolean = true,
        override val comment: String? = null
    ) : RecorderConfiguration

    /**
     * AsyncActionNavigation.union
     */

    @Test
    fun testAsyncActionNavigation_Union_AddToNullSectionId() {
        val configA = TestConfig("a")
        val configB = TestConfig("b")
        val configC = TestConfig("c")
        val configD = TestConfig("d")

        val nav1 = setOf(
            AsyncActionNavigation(null, setOf(configD), null),
            AsyncActionNavigation("foo", null, setOf(configC)))
        val nav2 = setOf(
            AsyncActionNavigation(null, setOf(configA, configB), null))
        val output1 = nav1.union(nav2)
        val expected1 = setOf(
            AsyncActionNavigation(null, setOf(configA, configB, configD), null),
            AsyncActionNavigation("foo", null, setOf(configC)))
        assertEquals(expected1, output1)
    }

    @Test
    fun testAsyncActionNavigation_Union_AddToFooSectionId() {
        val configA = TestConfig("a")
        val configB = TestConfig("b")
        val configC = TestConfig("c")
        val configD = TestConfig("d")

        val nav1 = setOf(
            AsyncActionNavigation(null, setOf(configD), null),
            AsyncActionNavigation("foo", null, setOf(configC)))
        val nav2 = setOf(
            AsyncActionNavigation("foo", setOf(configA, configB), null))
        val output1 = nav1.union(nav2)
        val expected1 = setOf(
            AsyncActionNavigation(null, setOf(configD), null),
            AsyncActionNavigation("foo", setOf(configA, configB), setOf(configC)))
        assertEquals(expected1, output1)
    }


    @Test
    fun testAsyncActionNavigation_Union_Subtract() {
        val configA = TestConfig("a")
        val configB = TestConfig("b")
        val configC = TestConfig("c")
        val configD = TestConfig("d")

        val nav1 = setOf(
            AsyncActionNavigation("foo", setOf(configD), setOf(configC)))
        val nav2 = setOf(
            AsyncActionNavigation("foo", setOf(configA, configC), setOf(configB)))
        val output1 = nav1.union(nav2)
        val expected1 = setOf(
            AsyncActionNavigation("foo", setOf(configA, configC, configD), setOf(configB)))
        assertEquals(expected1, output1)
    }


    /**
     * Start async actions
     */

    @Test
    fun testNodeAfter_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 0, "step").toList()
        val permissions =  listOf(PermissionInfoObject(PermissionType.Standard.Motion))
        val configA = TestConfig("a")
        val configB = TestConfig("b", "step3", "step4", permissions)
        val configC = TestConfig("c", null, "step3")
        val asyncList = listOf(configA, configB, configC)

        val assessmentObject = AssessmentObject(
            identifier = "foo",
            children = nodeList,
            backgroundActions = asyncList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        val point = navigator.nodeAfter(nodeList[2], result)
        assertEquals(nodeList[3], point.node)

        // There are no permissions to request that are *not* associated with an async action.
        assertNull(point.requestedPermissions)

        val expectedActions = setOf(AsyncActionNavigation("foo", setOf(configB), setOf(configC)))
        assertEquals(expectedActions, point.asyncActionNavigations)
    }

    @Test
    fun testNodeAfter_First_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 0, "step").toList()
        val permissions =  listOf(PermissionInfoObject(PermissionType.Standard.Motion))
        val configA = TestConfig("a")
        val configB = TestConfig("b", "step3", "step4", permissions)
        val configC = TestConfig("c", null, "step3")
        val asyncList = listOf(configA, configB, configC)

        val assessmentObject = AssessmentObject(
            identifier = "foo",
            children = nodeList,
            backgroundActions = asyncList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertTrue(navigator is NodeNavigator)
        val result = assessmentObject.createResult()

        val point = navigator.nodeAfter(null, result)
        assertEquals(nodeList[0], point.node)

        // There are no permissions to request that are *not* associated with an async action.
        assertNull(point.requestedPermissions)

        val expectedActions = setOf(AsyncActionNavigation("foo", setOf(configA, configC), null))
        assertEquals(expectedActions, point.asyncActionNavigations)
    }

    @Test
    fun testNodeAfter_Last_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 0, "step").toList()
        val permissions =  listOf(PermissionInfoObject(PermissionType.Standard.Motion))
        val configA = TestConfig("a")
        val configB = TestConfig("b", "step3", "step4", permissions)
        val configC = TestConfig("c", null, "step3")
        val asyncList = listOf(configA, configB, configC)

        val assessmentObject = AssessmentObject(
            identifier = "foo",
            children = nodeList,
            backgroundActions = asyncList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 4)

        val point = navigator.nodeAfter(nodeList[4], result)
        assertNull(point.node)

        // There are no permissions to request that are *not* associated with an async action.
        assertNull(point.requestedPermissions)

        val expectedActions = setOf(AsyncActionNavigation("foo", null, setOf(configA)))
        assertEquals(expectedActions, point.asyncActionNavigations)
    }
}

// Commented out to silence warning. syoung 05/12/2020
//fun Set<AsyncActionNavigation>.debugPrint() {
//    val idMap = this.map { async ->
//        Triple(
//            async.sectionIdentifier,
//            async.startAsyncActions?.map { it.identifier },
//            async.stopAsyncActions?.map { it.identifier }
//        )
//    }
//    println("$idMap")
//}