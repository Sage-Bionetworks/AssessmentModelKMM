import SwiftUI
import AssessmentModel

struct AssessmentInfoView: View {
    let assessment: AssessmentObject
    var body: some View {
        HStack {
            if let imageName = assessment.imageInfo?.imageName {
                Image(imageName)
            }
            VStack {
                Text(assessment.title ?? assessment.identifier)
                if let text = assessment.subtitle {
                    Text(text)
                }
            }
        }
    }
}

struct ContentView: View {
    @ObservedObject var viewModel = ContentViewViewModel()
    
    var body: some View {
        List {
            ForEach(0..<viewModel.assessments.count) { index in
                AssessmentInfoView(assessment: viewModel.assessments[index])
                    .onTapGesture {
                        viewModel.select(index)
                    }
            }
        }
        .fullScreenCover(item: $viewModel.currentAssessmentViewModel) { item in
            BranchNavigationView()
                .environmentObject(item)
        }
    }
}

open class ContentViewViewModel : ObservableObject, RootNodeController {
    @Published var currentAssessmentViewModel: BranchViewModel?
    
    let assessments: [AssessmentObject] = [
        previewPermissionsAssessment,
        previewSurveyAssessment,
    ]
    
    func select(_ index: Int) {
        guard currentAssessmentViewModel == nil else { return }
        let assessment = assessments[index]
        let branchState = BranchNodeStateImpl(node: assessment, parent: nil)
        branchState.rootNodeController = self
        currentAssessmentViewModel = BranchViewModel(branchState)
    }
    
    public func handleFinished(reason: FinishedReason, nodeState: NodeState) {
        // syoung 05/26/2021 For the example code, we just want to dismiss the view.
        currentAssessmentViewModel = nil
    }
    
    public func handleReadyToSave(reason: FinishedReason, nodeState: NodeState) {
        // syoung 05/26/2021 ignored in example app.
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

