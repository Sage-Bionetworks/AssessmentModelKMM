//
//  PagedNavigationViewModel.swift
//
//

import SwiftUI

/// A view model for a page-style navigation.
///
/// - SeeAlso: ``PagedNavigationBar``
public final class PagedNavigationViewModel : ObservableObject {
    
    /// The direction of the navigator.
    public enum Direction {
        case forward, backward
    }
    
    /// The number of pages in the model.
    @Published public var pageCount: Int {
        didSet {
            updateFraction()
        }
    }
    /// The current index (or page).
    @Published public var currentIndex: Int {
        didSet {
            updateFraction()
        }
    }
    /// Is forward enabled?
    @Published public var forwardEnabled: Bool = true
    /// Is back enabled?
    @Published public var backEnabled: Bool = false
    /// The current direction of navigation.
    @Published public var currentDirection: Direction = .forward
    /// The text to display on the "forward" button.
    @Published public var forwardButtonText: Text? = nil
    /// Should the progress indicator be hidden?
    @Published public var progressHidden: Bool
    /// progress as a fraction
    @Published public var fraction: Double = 0
    /// Is this navigator presenting? This flag can be used to dismiss a modal presentation.
    @Published public var isPresenting: Bool = true
    
    func updateFraction() {
        self.fraction = Double(currentIndex) / Double(pageCount > 0 ? pageCount : 1)
    }
    
    /// The action to call when the forward button is tapped. This will default to calling ``increment()``.
    lazy public var goForward: (() -> Void) = increment
    
    /// Increment the ``currentIndex`` and update the state of ``backEnabled`` and
    /// ``currentDirection``.
    public func increment() {
        self.currentDirection = .forward
        if self.currentIndex + 1 < self.pageCount {
            self.currentIndex += 1
        }
        else if isLoopingNavigator {
            self.currentIndex = 0
        }
        else {
            self.isPresenting = false
        }
        updateBackEnabled()
    }
    
    /// The action to call when the back button is tapped. This will default to calling ``decrement()``.
    lazy public var goBack: (() -> Void) = decrement

    /// Decrement the ``currentIndex`` and update the state of ``backEnabled`` and
    /// ``currentDirection``.
    public func decrement() {
        self.currentDirection = .backward
        if let nextIndex = indexBefore() {
            self.currentIndex = nextIndex
        }
        updateBackEnabled()
    }
    
    func updateBackEnabled() {
        self.backEnabled = isPresenting && (indexBefore().map { !backDisabledIndexes.contains($0) } ?? false)
    }
    
    func indexBefore() -> Int? {
        currentIndex > 0 ? currentIndex - 1 : ( isLoopingNavigator && pageCount > 0 ? pageCount - 1 : nil )
    }
    
    /// Does this navigator move in a loop?
    public let isLoopingNavigator: Bool
    
    /// A list of the index values where the back button should be disabled.
    public var backDisabledIndexes = Set<Int>()
    
    public init(pageCount: Int = 0, currentIndex: Int = 0, buttonText: Text? = nil, progressHidden: Bool = false, isLoopingNavigator: Bool = false) {
        self.pageCount = pageCount
        self.progressHidden = progressHidden
        self.currentIndex = currentIndex
        self.backEnabled = currentIndex > 0
        self.forwardButtonText = buttonText
        self.isLoopingNavigator = isLoopingNavigator
    }
}
