package org.sagebionetworks.assessmentmodel.navigation

import org.sagebionetworks.assessmentmodel.*
import org.sagebionetworks.assessmentmodel.serialization.*
import kotlin.test.*

class NodeNavigatorTest {

    @Test
    fun testNodeWithIdentifier() {
        val assessmentObject = AssessmentObject("foo", buildNodeList(5, 1, "step").toList())
        val navigator = assessmentObject.getNavigator()
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
        val navigator = assessmentObject.getNavigator()
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)

        val point = navigator.start()
        assertEquals(nodeList.first(), point.node)
        assertEquals(point.direction, NavigationPoint.Direction.Forward)
        val result = point.branchResult
        assertTrue(result is AssessmentResultObject)
        assertEquals(result.inputResults.count(), 0)
        assertEquals(result.pathHistoryResults.count(), 0)
        assertNull(point.requestedPermissions)
        assertNull(point.asyncActionNavigations)
    }

    @Test
    fun testNodeAfter_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.getNavigator()
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        assertTrue(navigator.hasNodeAfter(nodeList[2], result))

        val point = navigator.nodeAfter(nodeList[2], result)
        assertEquals(nodeList[3], point.node)
        assertEquals(NavigationPoint.Direction.Forward, point.direction)
        assertEquals(result, point.branchResult)
        assertNull(point.requestedPermissions)
        assertNull(point.asyncActionNavigations)
    }

    @Test
    fun testNodeAfter_LastNode_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.getNavigator()
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 4)

        assertFalse(navigator.hasNodeAfter(nodeList.last(), result))

        val point = navigator.nodeAfter(nodeList.last(), result)
        assertNull(point.node)
        assertEquals(NavigationPoint.Direction.Forward, point.direction)
        assertEquals(result, point.branchResult)
        assertNull(point.requestedPermissions)
        assertNull(point.asyncActionNavigations)
    }

    @Test
    fun testNodeBefore_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.getNavigator()
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 2)

        assertTrue(navigator.allowBackNavigation(nodeList[2], result))

        val point = navigator.nodeBefore(nodeList[2], result)
        assertEquals(nodeList[1], point.node)
        assertEquals(NavigationPoint.Direction.Backward, point.direction)
        assertEquals(result, point.branchResult)
        assertNull(point.requestedPermissions)
        assertNull(point.asyncActionNavigations)
    }

    @Test
    fun testNodeBefore_FirstNode_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val navigator = assessmentObject.getNavigator()
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 0)

        assertTrue(navigator.allowBackNavigation(nodeList[2], result))

        val point = navigator.nodeBefore(assessmentObject.children.first(), result)
        assertNull(point.node)
        assertEquals(NavigationPoint.Direction.Backward, point.direction)
        assertEquals(result, point.branchResult)
        assertNull(point.requestedPermissions)
        assertNull(point.asyncActionNavigations)
    }

    @Test
    fun testProgress_AllNodes_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = assessmentObject.allNodeIdentifiers()
        val navigator = assessmentObject.getNavigator()
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
        val navigator = assessmentObject.getNavigator()
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
        val navigator = assessmentObject.getNavigator()
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = buildResult(assessmentObject, 4)
        addResults(result, nodeList, 3, 2)

        assertTrue(navigator.allowBackNavigation(nodeList[2], result))

        val point = navigator.nodeBefore(nodeList[2], result)
        assertEquals(nodeList[1], point.node)
        assertEquals(NavigationPoint.Direction.Backward, point.direction)
        assertEquals(result, point.branchResult)
        assertNull(point.requestedPermissions)
        assertNull(point.asyncActionNavigations)
    }

    @Test
    fun testProgress_SomeNodes_Node2_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        assessmentObject.progressMarkers = listOf("step2", "step3", "step4")
        val navigator = assessmentObject.getNavigator()
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
        val navigator = assessmentObject.getNavigator()
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
        val navigator = assessmentObject.getNavigator()
        assertNotNull(navigator)
        assertTrue(navigator is NodeNavigator)
        val result = assessmentObject.createResult()
        val progress = navigator.progress(nodeList.last(), result)
        // Should not return a progress b/c the node is outside the progress marker range.
        assertNull(progress)
    }

    @Test
    fun testGoForward_Step3_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val testRootNodeController = TestRootNodeController("step3", 3)
        val expectedIdentifiers = listOf<String>("step1", "step2", "step3")

        nodeState.rootNodeController = testRootNodeController
        nodeState.goForward()

        assertFalse(testRootNodeController.infiniteLoop)
        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current result
        assertEquals(expectedIdentifiers.dropLast(1), topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedIdentifiers, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")
        assertEquals(expectedIdentifiers, testRootNodeController.pointChain.map { it.node?.identifier ?: "null" }, "${testRootNodeController.pointChain}")
        testRootNodeController.pointChain.forEach {
            assertEquals(it.direction, NavigationPoint.Direction.Forward)
        }
        val lastPoint = testRootNodeController.pointChain.last()
        assertEquals(lastPoint.branchResult, topResult)

        assertFalse(testRootNodeController.finished_called)
    }

    @Test
    fun testGoForward_Last_FlatLinearNavigation() {
        val nodeList = buildNodeList(5, 1, "step").toList()
        val assessmentObject = AssessmentObject("foo", nodeList)
        val nodeState = BranchNodeStateImpl(assessmentObject)

        val testRootNodeController = TestRootNodeController("end", 5)
        val expectedIdentifiers = listOf<String>("step1", "step2", "step3", "step4", "step5")

        nodeState.rootNodeController = testRootNodeController
        nodeState.goForward()

        assertFalse(testRootNodeController.infiniteLoop)
        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())

        val topResult = nodeState.currentResult
        // The path history should include the last one bc the finished should be called.
        assertEquals(expectedIdentifiers, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedIdentifiers, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")
        assertEquals(expectedIdentifiers, testRootNodeController.pointChain.map { it.node?.identifier ?: "null" }, "${testRootNodeController.pointChain}")
        testRootNodeController.pointChain.forEach {
            assertEquals(it.direction, NavigationPoint.Direction.Forward)
        }
        val lastPoint = testRootNodeController.pointChain.last()
        assertEquals(lastPoint.branchResult, topResult)

        assertTrue(testRootNodeController.finished_called)
        assertEquals(nodeState, testRootNodeController.finished_parent)
        assertNotNull(testRootNodeController.finished_navigationPoint)
        assertNull(testRootNodeController.finished_navigationPoint?.node)
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
        val testRootNodeController = TestRootNodeController("stepB3", expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.goForward()

        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())
        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(nodeB, nodeState.currentChild?.node)
        assertEquals(expectedResult, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")
        assertEquals(expectedChain, testRootNodeController.pointChain.map { it.node?.identifier ?: "null" }, "${testRootNodeController.pointChain}")
        testRootNodeController.pointChain.forEach {
            assertEquals(it.direction, NavigationPoint.Direction.Forward)
        }
        val lastPoint = testRootNodeController.pointChain.last()
        assertEquals(lastPoint.branchResult.identifier, "stepB")

        assertFalse(testRootNodeController.finished_called)
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
        val expectedLastPointResultIdentifier = "stepC"
        val expectedCurrentNode = nodeC
        val testRootNodeController = TestRootNodeController("stepC2", expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.goForward()

        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())
        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(expectedCurrentNode, nodeState.currentChild?.node)
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")
        assertEquals(expectedChain, testRootNodeController.pointChain.map { it.node?.identifier ?: "null" }, "${testRootNodeController.pointChain}")
        testRootNodeController.pointChain.forEach {
            assertEquals(NavigationPoint.Direction.Forward, it.direction)
        }
        val lastPoint = testRootNodeController.pointChain.last()
        assertEquals(expectedLastPointResultIdentifier, lastPoint.branchResult.identifier)

        assertFalse(testRootNodeController.finished_called)
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
        val expectedLastPointResultIdentifier = "foo"
        val expectedCurrentNode = nodeD
        val testRootNodeController = TestRootNodeController("end", expectedChain.count())

        nodeState.rootNodeController = testRootNodeController
        nodeState.goForward()

        assertEquals(testRootNodeController.expectedCount, testRootNodeController.nodeChain.count())
        assertFalse(testRootNodeController.infiniteLoop, "stepTo method may have hit an infinite loop")

        val topResult = nodeState.currentResult
        // The path history should be up to but not including the current node
        assertEquals(expectedCurrentNode, nodeState.currentChild?.node)
        assertEquals(expectedResults, topResult.pathHistoryResults.map { it.identifier }, "${topResult.pathHistoryResults}")
        // The node chains should include each node in the list to the testRootNodeController.stepTo value.
        assertEquals(expectedChain, testRootNodeController.nodeChain.map { it.node.identifier }, "${testRootNodeController.nodeChain}")
        assertEquals(expectedChain, testRootNodeController.pointChain.map { it.node?.identifier ?: "null" }, "${testRootNodeController.pointChain}")
        testRootNodeController.pointChain.forEach {
            assertEquals(NavigationPoint.Direction.Forward, it.direction)
        }
        val lastPoint = testRootNodeController.pointChain.last()
        assertEquals(expectedLastPointResultIdentifier, lastPoint.branchResult.identifier)

        assertTrue(testRootNodeController.finished_called)
        assertEquals(nodeState, testRootNodeController.finished_parent)
        assertNotNull(testRootNodeController.finished_navigationPoint)
        assertNull(testRootNodeController.finished_navigationPoint?.node)
    }

    @Test
    fun testAppendChildResultIfNeeded_DoNotReplaceUniqueLastResult() {
        val assessmentObject = AssessmentObject("foo", buildNodeList(3,1, "step").toList())
        val navigator = NodeNavigator(assessmentObject)
        val rootNodeController = TestRootNodeController(null, 999)
        val nodeState = BranchNodeStateImpl(assessmentObject)
        nodeState.rootNodeController = rootNodeController
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
        val assessmentObject = AssessmentObject("foo", buildNodeList(3,1, "step").toList())
        val navigator = NodeNavigator(assessmentObject)
        val rootNodeController = TestRootNodeController(null, 999)
        val nodeState = BranchNodeStateImpl(assessmentObject)
        nodeState.rootNodeController = rootNodeController
        nodeState.goForward()
        val testResult = ResultObject("step1")
        nodeState.currentResult.pathHistoryResults.add(testResult)
        // The expected behavior is that the step result should only be included *once*.
        nodeState.goForward()

        val expectedPathResults = listOf<Result>(testResult)
        assertEquals(expectedPathResults, nodeState.currentResult.pathHistoryResults)
    }

    @Test
    fun testAppendChildResultIfNeeded_StepNotAddedResult() {
        val assessmentObject = AssessmentObject("foo", buildNodeList(3,1, "step").toList())
        val navigator = NodeNavigator(assessmentObject)
        val rootNodeController = TestRootNodeController(null, 999)
        val nodeState = BranchNodeStateImpl(assessmentObject)
        nodeState.rootNodeController = rootNodeController
        nodeState.goForward()
        val testResult = ResultObject("step1")
        // The expected behavior is that the step result should be added.
        nodeState.goForward()

        val expectedPathResults = listOf<Result>(testResult)
        assertEquals(expectedPathResults, nodeState.currentResult.pathHistoryResults)
    }

    /**
     * Helper methods
     */

    data class TestResult(override val identifier: String, val answer: String) : Result

    class TestRootNodeController(val stepTo: String?, val expectedCount: Int) : RootNodeController {

        var infiniteLoop = false
        var nodeChain: MutableList<NodeState> = mutableListOf()
        var pointChain: MutableList<NavigationPoint> = mutableListOf()
        var finished_called = false
        var finished_navigationPoint: NavigationPoint? = null
        var finished_parent: BranchNodeState? = null

        override fun nodeStateFor(navigationPoint: NavigationPoint, parent: BranchNodeState): NodeState? {
            val node = navigationPoint.node
            return if (node is Step) StepNodeStateImpl(node, parent) else null
        }

        override fun show(nodeState: NodeState, navigationPoint: NavigationPoint) {
            nodeChain.add(nodeState)
            pointChain.add(navigationPoint)
            if (nodeChain.count() > expectedCount) {
                infiniteLoop = true
            }
            else if ((stepTo != null) && (nodeState.node.identifier != stepTo)) {
                nodeState.goForward()
            }
        }

        override fun handleFinished(navigationPoint: NavigationPoint, parent: BranchNodeState) {
            finished_called = true
            finished_navigationPoint = navigationPoint
            finished_parent = parent
        }
    }

    fun buildResult(assessmentObject: AssessmentObject, toIndex: Int) : AssessmentResult {
        val result = assessmentObject.createResult()
        addResults(result, assessmentObject.children, 0, toIndex)
        return result
    }

    fun addResults(result: BranchNodeResult, nodeList: List<Node>, fromIndex: Int, toIndex: Int) {
        val range = if (fromIndex < toIndex) (fromIndex..toIndex) else (fromIndex downTo toIndex)
        range.forEach {
            result.pathHistoryResults.add(nodeList[it].createResult())
        }
    }

    fun buildNodeList(nodeCount: Int, start: Int, prefix: String) : Sequence<InstructionStepObject>
        = generateSequence(start, { if ((it + 1) < (nodeCount + start)) (it + 1) else null }).map { InstructionStepObject(identifier = "$prefix$it") }
}