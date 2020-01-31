package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.AssessmentResult
import org.sagebionetworks.assessmentmodel.CollectionResult
import org.sagebionetworks.assessmentmodel.Node
import org.sagebionetworks.assessmentmodel.serialization.AssessmentObject
import org.sagebionetworks.assessmentmodel.serialization.AssessmentResultObject
import org.sagebionetworks.assessmentmodel.serialization.InstructionStepObject
import kotlin.test.*

class NodeNavigatorTest {

    @Test
    fun testNodeWithIdentifier() {
        val assessmentObject = AssessmentObject("foo", buildNodeList(5, 1, "step").toList())
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)

        val step1 = navigator.node("step1")
        assertNotNull(step1)
        assertEquals(step1.identifier, "step1")

        val step6 = navigator.node("step6")
        assertNull(step6)
    }

    @Test
    fun testStart_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)

        val point = navigator.start()
        assertEquals(nodeList.first(), point.node)
        assertEquals(point.direction, NavigationPoint.Direction.Forward)
        val result = point.result
        assertTrue(result is AssessmentResultObject)
        assertEquals(result.asyncActionResults.count(), 0)
        assertEquals(result.pathHistoryResults.count(), 0)
        assertNull(point.requestedPermissions)
        assertNull(point.startAsyncActions)
        assertNull(point.stopAsyncActions)
    }

    @Test
    fun testNodeAfter_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        assertTrue(navigator.hasNodeAfter(nodeList[2], result))

        val point = navigator.nodeAfter(nodeList[2], result)
        assertEquals(nodeList[3], point.node)
        assertEquals(NavigationPoint.Direction.Forward, point.direction)
        assertEquals(result, point.result)
        assertNull(point.requestedPermissions)
        assertNull(point.startAsyncActions)
        assertNull(point.stopAsyncActions)
    }

    @Test
    fun testNodeAfter_LastNode_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 4)

        assertFalse(navigator.hasNodeAfter(nodeList.last(), result))

        val point = navigator.nodeAfter(nodeList.last(), result)
        assertNull(point.node)
        assertEquals(NavigationPoint.Direction.Forward, point.direction)
        assertEquals(result, point.result)
        assertNull(point.requestedPermissions)
        assertNull(point.startAsyncActions)
        assertNull(point.stopAsyncActions)
    }

    @Test
    fun testNodeBefore_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        assertTrue(navigator.allowBackNavigation(nodeList[2], result))

        val point = navigator.nodeBefore(nodeList[2], result)
        assertEquals(nodeList[1], point.node)
        assertEquals(NavigationPoint.Direction.Backward, point.direction)
        assertEquals(result, point.result)
        assertNull(point.requestedPermissions)
        assertNull(point.startAsyncActions)
        assertNull(point.stopAsyncActions)
    }

    @Test
    fun testNodeBefore_FirstNode_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 0)

        assertTrue(navigator.allowBackNavigation(nodeList[2], result))

        val point = navigator.nodeBefore(assessmentObject.children.first(), result)
        assertNull(point.node)
        assertEquals(NavigationPoint.Direction.Backward, point.direction)
        assertEquals(result, point.result)
        assertNull(point.requestedPermissions)
        assertNull(point.startAsyncActions)
        assertNull(point.stopAsyncActions)
    }

    @Test
    fun testProgress_AllNodes_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = assessmentObject.allNodeIdentifiers()
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)
        val progress = navigator.progress(nodeList[2], result)

        val expected = Progress(2,5, false)
        assertEquals(expected, progress)
    }

    @Test
    fun testProgress_AllNodes_BackToNode2_FlatLinearNavigation() {
        val nodeList = buildNodeList(6, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = assessmentObject.allNodeIdentifiers()
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 4)
        addResults(result, nodeList, 3, 2)
        println("${result.pathHistoryResults}")
        val progress = navigator.progress(nodeList[2], result)

        val expected = Progress(2,6, false)
        assertEquals(expected, progress)
    }

    @Test
    fun testNodeBeforeProgress_BackToNode2_FlatLinearNavigation() {
        val nodeList = buildNodeList(6, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = assessmentObject.allNodeIdentifiers()
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 4)
        addResults(result, nodeList, 3, 2)

        assertTrue(navigator.allowBackNavigation(nodeList[2], result))

        val point = navigator.nodeBefore(nodeList[2], result)
        assertEquals(nodeList[1], point.node)
        assertEquals(NavigationPoint.Direction.Backward, point.direction)
        assertEquals(result, point.result)
        assertNull(point.requestedPermissions)
        assertNull(point.startAsyncActions)
        assertNull(point.stopAsyncActions)
    }

    @Test
    fun testProgress_SomeNodes_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = listOf("step2", "step3", "step4")
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)
        val progress = navigator.progress(nodeList[2], result)

        val expected = Progress(1,3, false)
        assertEquals(expected, progress)
    }

    @Test
    fun testProgress_SomeNodes_First_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = listOf("step2", "step3", "step4")
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = assessmentObject.createResult()
        val progress = navigator.progress(nodeList.first(), result)
        // Should not return a progress b/c the node is outside the progress marker range.
        assertNull(progress)
    }

    @Test
    fun testProgress_SomeNodes_Last_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = listOf("step2", "step3", "step4")
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = assessmentObject.createResult()
        val progress = navigator.progress(nodeList.last(), result)
        // Should not return a progress b/c the node is outside the progress marker range.
        assertNull(progress)
    }

    @Test
    fun testNodeAfter_Step_SectionNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.navigator
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        assertTrue(navigator.hasNodeAfter(nodeList[2], result))

        val point = navigator.nodeAfter(nodeList[2], result)
        assertEquals(nodeList[3], point.node)
        assertEquals(NavigationPoint.Direction.Forward, point.direction)
        assertEquals(result, point.result)
        assertNull(point.requestedPermissions)
        assertNull(point.startAsyncActions)
        assertNull(point.stopAsyncActions)
    }

    fun buildResult(assessmentObject: AssessmentObject, toIndex: Int) : AssessmentResult {
        val result = assessmentObject.createResult()
        addResults(result, assessmentObject.children, 0, toIndex)
        return result
    }

    fun addResults(result: CollectionResult, nodeList: List<Node>, fromIndex: Int, toIndex: Int) {
        val range = if (fromIndex < toIndex) (fromIndex..toIndex) else (fromIndex downTo toIndex)
        range.forEach {
            result.pathHistoryResults.add(nodeList[it].createResult())
        }
    }

    fun buildNodeList(nodeCount: Int, start: Int, prefix: String) : Sequence<InstructionStepObject>
        = generateSequence(start, { if ((it + 1) < (nodeCount + start)) (it + 1) else null }).map { InstructionStepObject(identifier = "$prefix$it") }
}