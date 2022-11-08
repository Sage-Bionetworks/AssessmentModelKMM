//
//  BranchViewModel.swift
//


import SwiftUI
import AssessmentModel
import MobilePassiveData
import SharedMobileUI

open class BranchViewModel : ObservableObject, NodeUIController, Identifiable {

    public let branchState: BranchNodeState
    
    @Published public var navigationViewModel: PagedNavigationViewModel = PagedNavigationViewModel()
    @Published public var currentNodeState: NodeState?
    
    public init(_ branchState: BranchNodeState) {
        self.branchState = branchState
        branchState.nodeUIController = self
        self.currentNodeState = branchState.currentChild
        self.navigationViewModel.goForward = self.goForward
        self.navigationViewModel.goBack = self.goBack
        if branchState.currentChild != nil {
            self.updateViewModel()
        }
        else {
            branchState.goForward()
        }
    }
    
    public var id: String {
        branchState.node.identifier
    }
    
    // MARK: NodeUIController
    
    /// By default, this view model can handle `ContentNode` views.
    open func canHandle(node: Node) -> Bool {
        node is ContentNodeStep
    }
    
    open func goForward() {
        let nodeState = self.currentNodeState ?? branchState
        nodeState.goForward()
    }
    
    open func goBack() {
        let nodeState = self.currentNodeState ?? branchState
        nodeState.goBack()
    }
    
    open func handleGoBack(nodeState: NodeState,
                           requestedPermissions: Set<AnyHashable>?,
                           asyncActionNavigations: Set<AsyncActionNavigation>?) {
        _handleNodeChange(nodeState: nodeState,
                          requestedPermissions: requestedPermissions,
                          asyncActionNavigations: asyncActionNavigations,
                          direction: .backward)
    }
    
    open func handleGoForward(nodeState: NodeState,
                              requestedPermissions: Set<AnyHashable>?,
                              asyncActionNavigations: Set<AsyncActionNavigation>?) {
        _handleNodeChange(nodeState: nodeState,
                          requestedPermissions: requestedPermissions,
                          asyncActionNavigations: asyncActionNavigations,
                          direction: .forward)
    }
    
    private var permissionsHandler: PermissionsHandler?
    
    private func _handleNodeChange(nodeState: NodeState,
                                   requestedPermissions: Set<AnyHashable>?,
                                   asyncActionNavigations: Set<AsyncActionNavigation>?,
                                   direction: NavigationPoint.Direction) {
        self.permissionsHandler = PermissionsHandler(requestedPermissions: requestedPermissions)
        self.permissionsHandler!.requestPermissions { [weak self] (failedPermission, error) in
            guard error == nil, failedPermission == nil, let strongSelf = self else {
                self?.handleFailedRequiredPermission(failedPermission, with: error)
                return
            }
            debugPrint(nodeState.node.identifier)
            strongSelf.navigationViewModel.currentDirection = (direction == .backward) ? .backward : .forward
            strongSelf.currentNodeState = nodeState
            strongSelf.updateViewModel()
            strongSelf.handleAsyncActionNavigations(asyncActionNavigations)
            strongSelf.permissionsHandler = nil
        }
    }
    
    open func updateViewModel() {
        if let progress = self.branchState.progress() {
            self.navigationViewModel.isEstimated = progress.isEstimated
            self.navigationViewModel.pageCount = Int(progress.total)
            self.navigationViewModel.currentIndex = Int(progress.current)
        }
        else {
            self.navigationViewModel.pageCount = 0
        }
        self.navigationViewModel.backEnabled = self.branchState.allowBackNavigation()
        // TODO: syoung 05/13/2021 enable/disable forward button based on whether or not all answers are valid.
    }
    
    // MARK: Future implementations

    func handleFailedRequiredPermission(_ failedPermssion: Permission?, with error: Error?) {
        // TODO: syoung 05/24/2021 Figure out how to show an alert to the user that they cannot continue b/c the permissions are required.
        // TODO: syoung 05/24/2021 Figure out how to bubble up that the task failed and needs to be cancelled b/c of required permission.
    }
    
    func handleAsyncActionNavigations(_ asyncActionNavigations: Set<AsyncActionNavigation>?) {
        // TODO: Implement syoung 05/13/2021
    }
}

extension NodeState {
    fileprivate func goForward() {
        self.goForward(requestedPermissions: nil, asyncActionNavigations: nil)
    }
    fileprivate func goBack() {
        self.goBackward(requestedPermissions: nil, asyncActionNavigations: nil)
    }
}

