//
//  NavigationRule.swift
//  
//
//  Copyright © 2017-2022 Sage Bionetworks. All rights reserved.
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

import Foundation

/// The navigation rule is used to allow the assessment navigator to check if a node has a navigation rule and
/// apply as necessary.
public protocol NavigationRule {

    /// Identifier for the next step to navigate to based on the current task result and the conditional rule associated
    /// with this task. The ``branchResult`` is the current result for the associated navigator. The variable
    /// ``isPeeking`` equals `true` if this is used in a call that sets up button state and equals `false` in a
    /// call used to navigate to the next node.
    func nextNodeIdentifier(branchResult: BranchNodeResult, isPeeking: Bool) -> String?
}

public protocol DirectNavigationRule : NavigationRule {
    /// The next node to jump to. This is used where direct navigation is required--for example, to allow the
    /// assessment to display information or a question on an alternate path and then exit the task. In that case,
    /// the main branch of navigation will need to "jump" over the alternate path step and the alternate path
    /// step will need to "jump" to the "exit".
    var nextNodeIdentifier: String? { get }
}

extension DirectNavigationRule {

    public func nextNodeIdentifier(branchResult: BranchNodeResult, isPeeking: Bool) -> String? {
        !isPeeking ? nextNodeIdentifier : nil
    }
}

