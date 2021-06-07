//
//  BranchViewModel.swift
//
//  Copyright © 2021 Sage Bionetworks. All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1.  Redistributions of source code must retain the above copyright notice, this
// list of conditions and the following disclaimer.
//
// 2.  Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation and/or
// other materials provided with the distribution.
//
// 3.  Neither the name of the copyright holder(s) nor the names of any contributors
// may be used to endorse or promote products derived from this software without
// specific prior written permission. No license is granted to the trademarks of
// the copyright holders even if such marks are included in this software.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//


import SwiftUI
import AssessmentModel
import MobilePassiveData

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
            strongSelf.navigationViewModel.currentDirection = direction
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

