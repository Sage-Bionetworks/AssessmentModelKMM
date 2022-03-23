package org.sagebionetworks.assessmentmodel.navigation

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.serialization.*
import org.sagebionetworks.assessmentmodel.survey.ReservedNavigationIdentifier
import kotlin.test.*

class NodeNavigatorTest : NavigationTestHelper() {

    /**
     * NodeNavigator - node(withIdentifier:)
     */

    @Test
    fun testNodeWithIdentifier() {
        val assessmentObject = AssessmentObject("foo", buildNodeList(5, 1, "step").toList())
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)

        val step1 = navigator.node("step1")
        assertNotNull(step1)
        assertEquals(step1.identifier, "step1")

        val step6 = navigator.node("step6")
        assertNull(step6)
    }

    /**
     * NodeNavigator - nodeAfter
     */

    @Test
    fun testStart_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)

        val point = navigator.nodeAfter(null, assessmentObject.createResult())
        assertEquals(nodeList.first(), point.node)
        assertEquals(point.direction, NavigationPoint.Direction.Forward)
        val result = point.branchResult
        assertTrue(result is AssessmentResultObject)
        assertEquals(result.inputResults.count(), 0)
        assertEquals(result.pathHistoryResults.count(), 0)
    }

    @Test
    fun testNodeAfter_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        assertTrue(navigator.hasNodeAfter(nodeList[2], result))

        val point = navigator.nodeAfter(nodeList[2], result)
        assertEquals(nodeList[3], point.node)
        assertEquals(NavigationPoint.Direction.Forward, point.direction)
        assertEquals(result, point.branchResult)
    }

    @Test
    fun testNodeAfter_LastNode_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 4)

        assertFalse(navigator.hasNodeAfter(nodeList.last(), result))

        val point = navigator.nodeAfter(nodeList.last(), result)
        assertNull(point.node)
        assertEquals(NavigationPoint.Direction.Forward, point.direction)
        assertEquals(result, point.branchResult)
    }

    @Test
    fun testNodeAfter_Node5_WithSkip() {
        val nodeList = buildNodeList(7, 1, "step").toList()
        nodeList[2].nextNodeIdentifier = "step5"

        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        assertTrue(navigator.hasNodeAfter(nodeList[2], result))

        val point = navigator.nodeAfter(nodeList[2], result)
        assertEquals(nodeList[4], point.node)
        assertEquals(NavigationPoint.Direction.Forward, point.direction)
        assertEquals(result, point.branchResult)
    }

    @Test
    fun testNodeAfter_Exit_WithSkip() {
        val nodeList = buildNodeList(7, 1, "step").toList()
        nodeList[2].nextNodeIdentifier = ReservedNavigationIdentifier.Exit.name

        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        assertTrue(navigator.hasNodeAfter(nodeList[2], result))

        val point = navigator.nodeAfter(nodeList[2], result)
        assertNull(point.node)
        assertEquals(NavigationPoint.Direction.Exit, point.direction)
        assertEquals(result, point.branchResult)
    }

    // TODO: syoung 03/07/2022 Decide how we want to handle navigation where there is a skipToIdentifier on the *result*
//    @Test
//    fun testNodeAfter_BackTo2_WithSkip() {
//        val nodeList = buildNodeList(7, 1, "step").toList()
//
//        val assessmentObject = AssessmentObject("foo", nodeList)
//        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
//        assertNotNull(navigator)
//        assertTrue(navigator is NodeNavigator)
//        val result = buildResult(assessmentObject, 4)
//        (result.pathHistoryResults.last() as ResultNavigationRule).nextNodeIdentifier = "step2"
//
//        assertTrue(navigator.hasNodeAfter(nodeList[4], result))
//
//        val point = navigator.nodeAfter(nodeList[4], result)
//        assertEquals(nodeList[1], point.node)
//        assertEquals(NavigationPoint.Direction.Backward, point.direction)
//        assertEquals(result, point.branchResult)
//        assertNull(point.requestedPermissions)
//        assertNull(point.asyncActionNavigations)
//
//        // And then go forward again.
//
//        result.pathHistoryResults.add(nodeList[1].createResult())
//        result.path.add(PathMarker("step2", NavigationPoint.Direction.Backward))
//
//        val nextPoint = navigator.nodeAfter(nodeList[1], result)
//        assertEquals(nodeList[2], nextPoint.node)
//        assertEquals(NavigationPoint.Direction.Forward, nextPoint.direction)
//        assertEquals(result, nextPoint.branchResult)
//        assertNull(nextPoint.requestedPermissions)
//        assertNull(nextPoint.asyncActionNavigations)
//    }

    /**
     * NodeNavigator - nodeBefore
     */

    @Test
    fun testNodeBefore_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        assertTrue(navigator.allowBackNavigation(nodeList[2], result))

        val point = navigator.nodeBefore(nodeList[2], result)
        assertEquals(nodeList[1], point.node)
        assertEquals(NavigationPoint.Direction.Backward, point.direction)
        assertEquals(result, point.branchResult)
    }

    @Test
    fun testNodeBefore_FirstNode_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 0)

        val currentNode = assessmentObject.children.first()
        assertFalse(navigator.allowBackNavigation(currentNode, result))

        val point = navigator.nodeBefore(currentNode, result)
        assertNull(point.node)
        assertEquals(NavigationPoint.Direction.Backward, point.direction)
        assertEquals(result, point.branchResult)
    }

    @Test
    fun testNodeBefore_Node5_WithSkip() {
        val nodeList = buildNodeList(7, 1, "step").toList()
        nodeList[2].nextNodeIdentifier = "step5"

        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        val currentNode = nodeList[4]
        result.pathHistoryResults.add(currentNode.createResult())
        result.path.add(PathMarker(currentNode.identifier, NavigationPoint.Direction.Forward))

        assertTrue(navigator.allowBackNavigation(currentNode, result))

        val point = navigator.nodeBefore(currentNode, result)
        assertEquals(nodeList[2], point.node)
        assertEquals(NavigationPoint.Direction.Backward, point.direction)
        assertEquals(result, point.branchResult)
    }

    /**
     * NodeNavigator - progress
     */

    @Test
    fun testProgress_AllNodes_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = assessmentObject.allNodeIdentifiers()
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)
        val progress = navigator.progress(nodeList[2], result)

        val expected = Progress(2, 5, false)
        assertEquals(expected, progress)
    }

    @Test
    fun testProgress_AllNodes_BackToNode2_FlatLinearNavigation() {
        val nodeList = buildNodeList(6, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = assessmentObject.allNodeIdentifiers()
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 4)
        addResults(result, nodeList, 3, 2)
        println("${result.pathHistoryResults}")
        val progress = navigator.progress(nodeList[2], result)

        val expected = Progress(2, 6, false)
        assertEquals(expected, progress)
    }

    @Test
    fun testNodeBeforeProgress_BackToNode2_FlatLinearNavigation() {
        val nodeList = buildNodeList(6, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = assessmentObject.allNodeIdentifiers()
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 4)
        addResults(result, nodeList, 3, 2)

        val node = nodeList[2]
        assertEquals(listOf(), node.hideButtons)
        assertTrue(navigator.allowBackNavigation(nodeList[2], result))

        val point = navigator.nodeBefore(nodeList[2], result)
        assertEquals(nodeList[1], point.node)
        assertEquals(NavigationPoint.Direction.Backward, point.direction)
        assertEquals(result, point.branchResult)
    }

    @Test
    fun testProgress_SomeNodes_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = listOf("step2", "step3", "step4")
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)
        val progress = navigator.progress(nodeList[2], result)

        val expected = Progress(1, 3, false)
        assertEquals(expected, progress)
    }

    @Test
    fun testProgress_SomeNodes_First_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = listOf("step2", "step3", "step4")
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
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
        val navigator = assessmentObject.createNavigator(BranchNodeStateImpl(assessmentObject, null))
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = assessmentObject.createResult()
        val progress = navigator.progress(nodeList.last(), result)
        // Should not return a progress b/c the node is outside the progress marker range.
        assertNull(progress)
    }

    /**
     * BranchNodeStateImpl - goForward
     */

    @Test
    fun testGoForward_Step3_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val testRootNodeController = TestRootNodeController(mapOf(NavigationPoint.Direction.Forward to "step3"), 3)
        val expectedIdentifiers = listOf("step1", "step2", "step3")
        val expectedPath = expectedIdentifiers.map { PathMarker(it, NavigationPoint.Direction.Forward) }

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()

        assertFalse(testRootNodeController.infiniteLoop)
        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current result
        assertEquals(expectedIdentifiers.dropLast(1), topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedIdentifiers, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")
        // Path should include all forward traverse.
        assertEquals(expectedPath, topResult.path)

        assertFalse(testRootNodeController.finished_called)
        assertFalse(testRootNodeController.readyToSave_called)
    }

    @Test
    fun testGoForward_Last_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val testRootNodeController = TestRootNodeController(mapOf(NavigationPoint.Direction.Forward to "end"), 5)
        val expectedIdentifiers = listOf("step1", "step2", "step3", "step4", "step5")

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()

        assertFalse(testRootNodeController.infiniteLoop)
        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())

        val topResult = nodeState.currentResult
        // The path history should include the last one bc the finished should be called.
        assertEquals(expectedIdentifiers, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedIdentifiers, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertTrue(testRootNodeController.finished_called)
        assertEquals(nodeState, testRootNodeController.finished_nodeState)

        assertTrue(testRootNodeController.readyToSave_called)
        assertEquals(nodeState, testRootNodeController.readyToSave_nodeState)
        assertFalse(testRootNodeController.finishedCalledBeforeSave)
    }

    @Test
    fun testGoForward_CompletionStep_FlatLinearNavigation() {
        val nodeList: MutableList<Node> = buildNodeList(5, 1, "step").toMutableList()
        val lastNode = TestCompletionStep("completion")
        nodeList.add(lastNode)
        val assessmentObject = AssessmentObject("foo", nodeList)
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val testRootNodeController = TestRootNodeController(mapOf(NavigationPoint.Direction.Forward to "completion"), 6)
        val expectedIdentifiers = listOf("step1", "step2", "step3", "step4", "step5", "completion")
        val expectedPath = expectedIdentifiers.map { PathMarker(it, NavigationPoint.Direction.Forward) }

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()

        assertFalse(testRootNodeController.infiniteLoop)
        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current result
        assertEquals(expectedIdentifiers, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedIdentifiers, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")
        // Path should include all forward traverse.
        assertEquals(expectedPath, topResult.path)

        assertFalse(testRootNodeController.finished_called)

        assertFalse(lastNode.canGoBack())
        assertTrue(testRootNodeController.readyToSave_called)
        assertEquals(nodeState, testRootNodeController.readyToSave_nodeState)
    }

    @Test
    fun testGoForward_StepB3_SectionNavigation() {
        val nodeA = InstructionStepObject("stepA")
        val nodeListB = buildNodeList(5, 1, "stepB").toList()
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC = buildNodeList(3, 1, "stepC").toList()
        val nodeC = SectionObject("stepC", nodeListC)
        val nodeD = InstructionStepObject("stepD")
        val assessmentObject = AssessmentObject("foo", listOf(nodeA, nodeB, nodeC, nodeD))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepA", "stepB1", "stepB2", "stepB3")
        val expectedResult = listOf("stepA")
        val testRootNodeController = TestRootNodeController(mapOf(NavigationPoint.Direction.Forward to "stepB3"), expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()

        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())
        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeB, nodeState.currentChild?.node)
        assertEquals(expectedResult, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertFalse(testRootNodeController.finished_called)
        assertFalse(testRootNodeController.readyToSave_called)
    }

    @Test
    fun testGoForward_FirstSection_SectionNavigation() {
        val nodeListB: MutableList<Node> = buildNodeList(4, 1, "stepB").toMutableList()
        nodeListB.add(TestCompletionStep("stepB5"))
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC = buildNodeList(3, 1, "stepC").toList()
        val nodeC = SectionObject("stepC", nodeListC)
        val nodeD = InstructionStepObject("stepD")
        val assessmentObject = AssessmentObject("foo", listOf(nodeB, nodeC, nodeD))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepB1", "stepB2", "stepB3", "stepB4", "stepB5")
        val expectedResult = listOf<String>()
        val testRootNodeController = TestRootNodeController(mapOf(NavigationPoint.Direction.Forward to "stepB5"), expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()

        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())
        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeB, nodeState.currentChild?.node)
        assertEquals(expectedResult, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertFalse(testRootNodeController.finished_called)
        assertFalse(testRootNodeController.readyToSave_called)
    }

    @Test
    fun testGoForward_StepC2_SectionNavigation() {
        val nodeA = InstructionStepObject("stepA")
        val nodeListB = buildNodeList(5, 1, "stepB").toList()
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC = buildNodeList(3, 1, "stepC").toList()
        val nodeC = SectionObject("stepC", nodeListC)
        val nodeD = InstructionStepObject("stepD")
        val assessmentObject = AssessmentObject("foo", listOf(nodeA, nodeB, nodeC, nodeD))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepA", "stepB1", "stepB2", "stepB3", "stepB4", "stepB5", "stepC1", "stepC2")
        val expectedResults = listOf("stepA", "stepB")
        val testRootNodeController = TestRootNodeController(mapOf(NavigationPoint.Direction.Forward to "stepC2"), expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()

        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())
        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeC, nodeState.currentChild?.node)
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertFalse(testRootNodeController.finished_called)
        assertFalse(testRootNodeController.readyToSave_called)

        // Test that calling goForward() on the root will still go forward
        println("reset stepTo")
        testRootNodeController.expectedCount += 1
        testRootNodeController.stepTo = mapOf(NavigationPoint.Direction.Forward to "stepC3")
        nodeState.goForward()

        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")
        assertEquals(nodeC, nodeState.currentChild?.node)

        val sectionState = nodeState.currentChild
        assertTrue(sectionState is BranchNodeStateImpl)
        assertEquals("stepC3", sectionState.currentChild?.node?.identifier)

        val expectedNewChain = expectedChain.plus("stepC3")
        assertEquals(expectedNewChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")
    }

    @Test
    fun testGoForward_Last_SectionNavigation() {
        val nodeA = InstructionStepObject("stepA")
        val nodeListB = buildNodeList(5, 1, "stepB").toList()
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC = buildNodeList(3, 1, "stepC").toList()
        val nodeC = SectionObject("stepC", nodeListC)
        val nodeD = InstructionStepObject("stepD")
        val assessmentObject = AssessmentObject("foo", listOf(nodeA, nodeB, nodeC, nodeD))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepA", "stepB1", "stepB2", "stepB3", "stepB4", "stepB5", "stepC1", "stepC2", "stepC3", "stepD")
        val expectedResults = listOf("stepA", "stepB", "stepC", "stepD")
        val testRootNodeController = TestRootNodeController(mapOf(NavigationPoint.Direction.Forward to "end"), expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()

        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())
        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeD, nodeState.currentChild?.node)
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertTrue(testRootNodeController.finished_called)
        assertEquals(nodeState, testRootNodeController.finished_nodeState)

        assertTrue(testRootNodeController.readyToSave_called)
        assertEquals(nodeState, testRootNodeController.readyToSave_nodeState)
        assertFalse(testRootNodeController.finishedCalledBeforeSave)
    }

    @Test
    fun testGoForward_Completion_SectionNavigation() {
        val nodeA = InstructionStepObject("stepA")
        val nodeListB = buildNodeList(5, 1, "stepB").toList()
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC = buildNodeList(3, 1, "stepC").toList()
        val nodeC = SectionObject("stepC", nodeListC)
        val nodeD = TestCompletionStep("stepD")
        val assessmentObject = AssessmentObject("foo", listOf(nodeA, nodeB, nodeC, nodeD))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepA", "stepB1", "stepB2", "stepB3", "stepB4", "stepB5", "stepC1", "stepC2", "stepC3", "stepD")
        val expectedResults = listOf("stepA", "stepB", "stepC", "stepD")
        val testRootNodeController = TestRootNodeController(mapOf(NavigationPoint.Direction.Forward to "stepD"), expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()

        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())
        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeD, nodeState.currentChild?.node)
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertFalse(testRootNodeController.finished_called)

        assertTrue(testRootNodeController.readyToSave_called)
        assertEquals(nodeState, testRootNodeController.readyToSave_nodeState)
        assertFalse(testRootNodeController.finishedCalledBeforeSave)
    }

    @Test
    fun testGoForward_CompletionInSection_SectionNavigation() {
        val nodeA = InstructionStepObject("stepA")
        val nodeListB: MutableList<Node> = buildNodeList(4, 1, "stepB").toMutableList()
        nodeListB.add(TestCompletionStep("stepB5"))
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC: MutableList<Node> = buildNodeList(2, 1, "stepC").toMutableList()
        nodeListC.add(TestCompletionStep("stepC3"))
        val nodeC = SectionObject("stepC", nodeListC)
        val assessmentObject = AssessmentObject("foo", listOf(nodeA, nodeB, nodeC))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepA", "stepB1", "stepB2", "stepB3", "stepB4", "stepB5", "stepC1", "stepC2", "stepC3")
        val expectedResults = listOf("stepA", "stepB", "stepC")
        val testRootNodeController = TestRootNodeController(mapOf(NavigationPoint.Direction.Forward to "stepC3"), expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()

        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())
        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        val lastResult = topResult.pathHistoryResults.last()
        assertTrue(lastResult is BranchNodeResult)
        val expectedResultsC = listOf("stepC1", "stepC2", "stepC3")
        assertEquals(expectedResultsC, lastResult.pathHistoryResults.map { it.identifier }, "${lastResult.pathHistoryResults}")

        assertFalse(testRootNodeController.finished_called)

        assertTrue(testRootNodeController.readyToSave_called)
        assertEquals(nodeState, testRootNodeController.readyToSave_nodeState)
        assertFalse(testRootNodeController.finishedCalledBeforeSave)
    }

    @Test
    fun testGoForward_Exit_SectionNavigation() {
        val nodeA = InstructionStepObject("stepA")
        val nodeListB = buildNodeList(5, 1, "stepB").toList()
        nodeListB[2].nextNodeIdentifier = ReservedNavigationIdentifier.Exit.name
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC = buildNodeList(3, 1, "stepC").toList()
        val nodeC = SectionObject("stepC", nodeListC)
        val nodeD = InstructionStepObject("stepD")
        val assessmentObject = AssessmentObject("foo", listOf(nodeA, nodeB, nodeC, nodeD))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepA", "stepB1", "stepB2", "stepB3")
        val expectedResults = listOf("stepA", "stepB")
        val testRootNodeController = TestRootNodeController(mapOf(NavigationPoint.Direction.Forward to "end"), expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()

        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())
        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeB, nodeState.currentChild?.node)
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertTrue(testRootNodeController.finished_called)
        assertEquals(nodeState, testRootNodeController.finished_nodeState)

        assertTrue(testRootNodeController.readyToSave_called)
        assertEquals(nodeState, testRootNodeController.readyToSave_nodeState)
        assertFalse(testRootNodeController.finishedCalledBeforeSave)
    }

    /**
     * BranchNodeStateImpl - goBackward
     */

    @Test
    fun testGoBackward_Step2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedIdentifiers = listOf("step1", "step2", "step3", "step2")
        val testRootNodeController = TestRootNodeController(mapOf(
                NavigationPoint.Direction.Forward to "step3",
                NavigationPoint.Direction.Backward to "step2"),
                expectedIdentifiers.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()
        nodeState.goBackward()

        assertFalse(testRootNodeController.infiniteLoop)
        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current result
        assertEquals(expectedIdentifiers.dropLast(1), topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedIdentifiers, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertFalse(testRootNodeController.finished_called)
        assertFalse(testRootNodeController.readyToSave_called)
    }

    @Test
    fun testGoBackward_StepB5_SectionNavigation() {
        val nodeA = InstructionStepObject("stepA")
        val nodeListB = buildNodeList(5, 1, "stepB").toList()
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC = buildNodeList(3, 1, "stepC").toList()
        val nodeC = SectionObject("stepC", nodeListC)
        val nodeD = InstructionStepObject("stepD")
        val assessmentObject = AssessmentObject("foo", listOf(nodeA, nodeB, nodeC, nodeD))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepA", "stepB1", "stepB2", "stepB3", "stepB4", "stepB5", "stepC1", "stepB5", "stepB4")
        val expectedResults = listOf("stepA", "stepB", "stepC")
        val testRootNodeController = TestRootNodeController(mapOf(
                NavigationPoint.Direction.Forward to "stepC1",
                NavigationPoint.Direction.Backward to "stepB4"),
                expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()
        assertEquals(nodeC, nodeState.currentChild?.node)
        nodeState.goBackward()

        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeB, nodeState.currentChild?.node)
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertFalse(testRootNodeController.finished_called)
        assertFalse(testRootNodeController.readyToSave_called)
    }

    @Test
    fun testGoBackward_StepC1_SectionNavigation() {
        val nodeA = InstructionStepObject("stepA")
        val nodeListB = buildNodeList(5, 1, "stepB").toList()
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC = buildNodeList(3, 1, "stepC").toList()
        val nodeC = SectionObject("stepC", nodeListC)
        val nodeD = InstructionStepObject("stepD")
        val assessmentObject = AssessmentObject("foo", listOf(nodeA, nodeB, nodeC, nodeD))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepA", "stepB1", "stepB2", "stepB3", "stepB4", "stepB5", "stepC1", "stepC2", "stepC3", "stepC2", "stepC1")
        val expectedResults = listOf("stepA", "stepB")
        val testRootNodeController = TestRootNodeController(mapOf(
                NavigationPoint.Direction.Forward to "stepC3",
                NavigationPoint.Direction.Backward to "stepC1"),
                expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()
        assertEquals(nodeC, nodeState.currentChild?.node)
        nodeState.goBackward()

        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeC, nodeState.currentChild?.node)
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertFalse(testRootNodeController.finished_called)
        assertFalse(testRootNodeController.readyToSave_called)
    }

    @Test
    fun testGoBackward_StepB3_SectionNavigation() {
        val nodeA = InstructionStepObject("stepA")
        val nodeListB = buildNodeList(5, 1, "stepB").toList()
        nodeListB[2].nextNodeIdentifier = "stepB5"
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC = buildNodeList(3, 1, "stepC").toList()
        val nodeC = SectionObject("stepC", nodeListC)
        val nodeD = InstructionStepObject("stepD")
        val assessmentObject = AssessmentObject("foo", listOf(nodeA, nodeB, nodeC, nodeD))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepA", "stepB1", "stepB2", "stepB3", "stepB5", "stepC1", "stepC2", "stepC3", "stepC2", "stepC1", "stepB5", "stepB3")
        val expectedResults = listOf("stepA", "stepB", "stepC")
        val testRootNodeController = TestRootNodeController(mapOf(
            NavigationPoint.Direction.Forward to "stepC3",
            NavigationPoint.Direction.Backward to "stepB3"),
            expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()
        assertEquals(nodeC, nodeState.currentChild?.node)
        nodeState.goBackward()

        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeB, nodeState.currentChild?.node)
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertFalse(testRootNodeController.finished_called)
        assertFalse(testRootNodeController.readyToSave_called)
    }

    @Test
    fun testGoBackward_StepB3_FromNextSection_SectionNavigation() {
        val nodeA = InstructionStepObject("stepA")
        val nodeListB = buildNodeList(5, 1, "stepB").toList()
        nodeListB[2].nextNodeIdentifier = ReservedNavigationIdentifier.NextSection.name
        val nodeB = SectionObject("stepB", nodeListB)
        val nodeListC = buildNodeList(3, 1, "stepC").toList()
        val nodeC = SectionObject("stepC", nodeListC)
        val nodeD = InstructionStepObject("stepD")
        val assessmentObject = AssessmentObject("foo", listOf(nodeA, nodeB, nodeC, nodeD))
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val expectedChain = listOf("stepA", "stepB1", "stepB2", "stepB3", "stepC1", "stepC2", "stepC3", "stepC2", "stepC1", "stepB3", "stepB2")
        val expectedResults = listOf("stepA", "stepB", "stepC")
        val testRootNodeController = TestRootNodeController(mapOf(
            NavigationPoint.Direction.Forward to "stepC3",
            NavigationPoint.Direction.Backward to "stepB2"),
            expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.nodeUIController = testRootNodeController
        nodeState.goForward()
        assertEquals(nodeC, nodeState.currentChild?.node)
        nodeState.goBackward()

        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeB, nodeState.currentChild?.node)
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")

        assertFalse(testRootNodeController.finished_called)
        assertFalse(testRootNodeController.readyToSave_called)
    }

    /**
     * BranchNodeStateImpl - appendChildResultIfNeeded
     */

    @Test
    fun testAppendChildResultIfNeeded_DoNotReplaceUniqueLastResult() {
        val assessmentObject = AssessmentObject("foo", buildNodeList(3, 1, "step").toList())
        val rootNodeController = TestRootNodeController(expectedCount = 999)
        val nodeState = BranchNodeStateImpl(assessmentObject)
        nodeState.rootNodeController = rootNodeController
        nodeState.nodeUIController = rootNodeController
        nodeState.goForward()
        val testResult = TestResult("step1", "magoo")
        nodeState.currentResult.pathHistoryResults.add(testResult)

        // The expected behavior is that b/c the controller has added a result that does not match the state step
        // result, *that* result should be honored.
        nodeState.goForward()

        val expectedPathResults = listOf<Result>(testResult)
        assertEquals(expectedPathResults, nodeState.currentResult.pathHistoryResults)
    }

    @Test
    fun testAppendChildResultIfNeeded_IncludeEqualStepResult() {
        val assessmentObject = AssessmentObject("foo", buildNodeList(3, 1, "step").toList())
        val rootNodeController = TestRootNodeController(expectedCount = 999)
        val nodeState = BranchNodeStateImpl(assessmentObject)
        nodeState.rootNodeController = rootNodeController
        nodeState.nodeUIController = rootNodeController
        nodeState.goForward()
        val testResult = ResultObject("step1")
        nodeState.currentResult.pathHistoryResults.add(testResult)
        // The expected behavior is that the step result should only be included *once*.
        nodeState.goForward()

        val actualPath = nodeState.currentResult.pathHistoryResults.map { it.identifier }
        assertEquals(listOf("step1"), actualPath)
    }

    @Test
    fun testAppendChildResultIfNeeded_StepNotAddedResult() {
        val assessmentObject = AssessmentObject("foo", buildNodeList(3, 1, "step").toList())
        val rootNodeController = TestRootNodeController(expectedCount = 999)
        val nodeState = BranchNodeStateImpl(assessmentObject)
        nodeState.rootNodeController = rootNodeController
        nodeState.nodeUIController = rootNodeController
        nodeState.goForward()
        // The expected behavior is that the step result should be added.
        nodeState.goForward()

        val actualPath = nodeState.currentResult.pathHistoryResults.map { it.identifier }
        assertEquals(listOf("step1"), actualPath)
    }
}

private data class TestCompletionStep(override val identifier: String,
                                      override val comment: String? = null,
                                      override val hideButtons: List<ButtonAction> = listOf(),
                                      override val buttonMap: Map<ButtonAction, ButtonActionInfo> = mapOf(),
                                      override val spokenInstructions: Map<SpokenInstructionTiming, String>? = null
) : CompletionStep

open class NavigationTestHelper {

    /**
     * Helper methods
     */

    data class TestResult(override val identifier: String,
                          val answer: String
    ) : Result {

        // TODO: syoung 06/16/2020 Once timestamp generation is implemented for Android (which is the platform used for test)
        // then add checks that the dates are being updated properly to mark begin/end of steps.

        override var startDateTime: Instant = Clock.System.now()
        override var endDateTime: Instant? = null

        override fun copyResult(identifier: String): Result = copy(identifier = identifier)
    }

    class TestRootNodeController(var stepTo: Map<NavigationPoint.Direction, String> = mapOf(), var expectedCount: Int) : NodeUIController, RootNodeController {

        var infiniteLoop = false
        var nodeChain: MutableList<NodeState> = mutableListOf()
        var finished_called = false
        var finished_nodeState: NodeState? = null
        var finished_reason: FinishedReason? = null
        var readyToSave_called = false
        var readyToSave_nodeState: NodeState? = null
        var readyToSave_reason: FinishedReason? = null
        var finishedCalledBeforeSave = false

        override fun canHandle(node: Node): Boolean {
            return (node is Step)
        }

        override fun handleGoBack(nodeState: NodeState) {
            show(nodeState, NavigationPoint.Direction.Backward)
        }

        override fun handleGoForward(nodeState: NodeState) {
            show(nodeState, NavigationPoint.Direction.Forward)
        }

        override fun handleReadyToSave(reason: FinishedReason, nodeState: NodeState) {
            readyToSave_called = true
            readyToSave_nodeState = nodeState
            readyToSave_reason = reason
        }

        private fun show(nodeState: NodeState, direction: NavigationPoint.Direction) {
            nodeChain.add(nodeState)
            if (nodeChain.count() > expectedCount) {
                infiniteLoop = true
                return
            }
            val stepToIdentifier = stepTo[direction]
            if ((stepToIdentifier != null) && (nodeState.node.identifier != stepToIdentifier)) {
                nodeState.goIn(direction)
            }
        }

        override fun handleFinished(reason: FinishedReason, nodeState: NodeState) {
            finished_called = true
            finished_nodeState = nodeState
            finished_reason = reason
            finishedCalledBeforeSave = !readyToSave_called
        }
    }

    fun buildResult(assessmentObject: AssessmentObject, toIndex: Int) : AssessmentResult {
        val result = assessmentObject.createResult()
        addResults(result, assessmentObject.children, 0, toIndex)
        return result
    }

    fun addResults(result: BranchNodeResult, nodeList: List<Node>, fromIndex: Int, toIndex: Int) {
        val direction = if (fromIndex <= toIndex) NavigationPoint.Direction.Forward else NavigationPoint.Direction.Backward
        val range = if (fromIndex < toIndex) (fromIndex..toIndex) else (fromIndex downTo toIndex)
        range.forEach {
            result.pathHistoryResults.add(nodeList[it].createResult())
            result.path.add(PathMarker(nodeList[it].identifier, direction))
        }
    }

    fun buildNodeList(nodeCount: Int, start: Int, prefix: String) : Sequence<InstructionStepObject>
        = generateSequence(start, { if ((it + 1) < (nodeCount + start)) (it + 1) else null }).map { InstructionStepObject(identifier = "$prefix$it") }
}