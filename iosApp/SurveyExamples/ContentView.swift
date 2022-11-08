//
//  ContentView.swift
//  SurveyExamples
//

import SwiftUI
import AssessmentModel
import AssessmentModelUI

struct ContentView: View {
    @StateObject var viewModel: ViewModel = .init()
    let surveys: [AssessmentHolder] = previewExamples.map { .init(assessment: $0) }

    var body: some View {
        List(surveys) { holder in
            Button(holder.assessment.title ?? holder.id) {
                viewModel.current = .init(holder.assessment)
            }
        }
        .fullScreenCover(isPresented: $viewModel.isPresented) {
            AssessmentListener(viewModel)
        }
    }
    
    class ViewModel : ObservableObject {
        @Published var isPresented: Bool = false
        var current: AssessmentState? {
            didSet {
                isPresented = (current != nil)
            }
        }
    }
    
    struct AssessmentListener : View {
        @ObservedObject var viewModel: ViewModel
        @ObservedObject var state: AssessmentState
        
        init(_ viewModel: ViewModel) {
            self.viewModel = viewModel
            self.state = viewModel.current!
        }
        
        var body: some View {
            AssessmentView(state)
                .onChange(of: state.status) { newValue in
                    print("assessment status = \(newValue)")
                    guard newValue >= .finished else { return }
                    print("\(try! state.result.jsonDictionary())")
                    // In a real use-case this is where you might save and upload data
                    viewModel.isPresented = false
                    viewModel.current = nil
                }
        }
    }
}

struct AssessmentHolder : Identifiable {
    var id: String { assessment.identifier }
    let assessment: Assessment
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
