#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class AssessmentModelActiveStepCommand, AssessmentModelSpokenInstructionTiming, AssessmentModelKotlinx_serialization_jsonJson, AssessmentModelButtonAction, AssessmentModelKotlinEnum<E>, AssessmentModelAnswerType, AssessmentModelKotlinx_serialization_jsonJsonElement, AssessmentModelPathMarker, AssessmentModelButtonActionCustom, AssessmentModelButtonActionNavigation, AssessmentModelKotlinArray<T>, AssessmentModelButtonStyle, AssessmentModelButtonStyleFooter, AssessmentModelButtonStyleNavigationHeader, AssessmentModelProduct, AssessmentModelNavigationPointDirection, AssessmentModelPermissionType, AssessmentModelPermissionInfoObject, AssessmentModelPermissionTypeCustom, AssessmentModelPermissionTypeStandard, AssessmentModelIdentifierPath, AssessmentModelSpokenInstructionTimingKeyword, AssessmentModelSpokenInstructionTimingTimeInterval, AssessmentModelBaseType, AssessmentModelKotlinx_serialization_coreSerialKind, AssessmentModelAnswerTypeArray, AssessmentModelAnswerTypeDateTime, AssessmentModelAnswerTypeMeasurement, AssessmentModelAnyNodeFieldStateImpl, AssessmentModelAutoCapitalizationType, AssessmentModelAutoCorrectionType, AssessmentModelUIHint, AssessmentModelFetchableImage, AssessmentModelSurveyRuleOperator, AssessmentModelDateTimePart, AssessmentModelDatePart, AssessmentModelDateTimeReference, AssessmentModelDateTime, AssessmentModelDateTimeComponents, NSDate, NSTimeZone, AssessmentModelAsyncActionNavigation, AssessmentModelFormattedValue<T>, AssessmentModelISO8601Format, AssessmentModelInvalidMessageObject, AssessmentModelKeyboardType, AssessmentModelSpellCheckingType, AssessmentModelNumberType, AssessmentModelAbstractQuestionFieldStateImpl, AssessmentModelReservedNavigationIdentifier, AssessmentModelTimePart, AssessmentModelUIHintChoice, AssessmentModelUIHintCustom, AssessmentModelUIHintDetail, AssessmentModelUIHintTextField, AssessmentModelAttitudeReferenceFrame, AssessmentModelDistanceRecord, AssessmentModelDistanceRecorderConfiguration, AssessmentModelMotionRecorderType, AssessmentModelMotionRecord, AssessmentModelMotionRecorderConfiguration, AssessmentModelNodeObject, AssessmentModelViewThemeObject, AssessmentModelStepObject, AssessmentModelBaseActiveStepObject, AssessmentModelActiveStepObject, AssessmentModelImagePlacement, AssessmentModelSize, AssessmentModelAnimatedImage, AssessmentModelAnswerResultObject, AssessmentModelAssessmentGroupInfoObject, AssessmentModelKotlinDecoder, AssessmentModelKotlinDecodable, NSBundle, AssessmentModelAssessmentGroupWrapper, AssessmentModelAssessmentLoader, AssessmentModelIconNodeObject, AssessmentModelNodeContainerObject, AssessmentModelAssessmentObject, AssessmentModelAssessmentResultObject, AssessmentModelBranchNodeResultObject, AssessmentModelSerializableButtonActionInfo, AssessmentModelButtonActionInfoObject, AssessmentModelCheckboxInputItemObject, AssessmentModelChoiceItemWrapper, AssessmentModelChoiceOptionObject, AssessmentModelComparableSurveyRuleObject, AssessmentModelQuestionObject, AssessmentModelChoiceQuestionObject, AssessmentModelCollectionResultObject, AssessmentModelComboBoxQuestionObject, AssessmentModelStringTextInputItemObject, AssessmentModelCountdownStepObject, AssessmentModelDateFormatOptions, AssessmentModelInputItemObject, AssessmentModelDateInputItemObject, AssessmentModelDoubleFormatOptions, AssessmentModelDecimalTextInputItemObject, AssessmentModelNumberFormatOptionsStyle, AssessmentModelNumberFormatOptions<T>, NSNumber, AssessmentModelNumberFormatter<T>, AssessmentModelFormStepObject, AssessmentModelIconInfoObject, AssessmentModelImagePlacementCustom, AssessmentModelImagePlacementStandard, AssessmentModelInstructionStepObject, AssessmentModelIntFormatOptions, AssessmentModelKeyboardOptionsObject, AssessmentModelIntegerTextInputItemObject, AssessmentModelMultipleInputQuestionObject, AssessmentModelNavigationButtonActionInfoObject, AssessmentModelOverviewStepObject, AssessmentModelRegExValidator, AssessmentModelReminderButtonActionInfoObject, AssessmentModelResultObject, AssessmentModelResultSummaryStepObject, AssessmentModelSectionObject, AssessmentModelKotlinx_serialization_coreSerializersModule, AssessmentModelSimpleQuestionObject, AssessmentModelSkipCheckboxInputItemObject, AssessmentModelStringChoiceQuestionObject, AssessmentModelTimeFormatOptions, AssessmentModelTimeInputItemObject, AssessmentModelTransformableAssessmentObject, AssessmentModelTransformableNodeObject, AssessmentModelVideoViewButtonActionInfoObject, AssessmentModelWebViewButtonActionInfoObject, AssessmentModelYearFormatOptions, AssessmentModelYearTextInputItemObject, AssessmentModelNavigationPoint, AssessmentModelFinishedReason, AssessmentModelProgress, AssessmentModelKotlinError, NSDateFormatter, AssessmentModelKotlinNothing, AssessmentModelKotlinThrowable;

@protocol AssessmentModelViewTheme, AssessmentModelNode, AssessmentModelFileLoader, AssessmentModelResourceInfo, AssessmentModelButtonActionInfo, AssessmentModelResult, AssessmentModelResultMapElement, AssessmentModelStep, AssessmentModelKotlinComparable, AssessmentModelStringEnum, AssessmentModelAssetInfo, AssessmentModelAssetResourceInfo, AssessmentModelDrawableLayout, AssessmentModelImageInfo, AssessmentModelNavigator, AssessmentModelBranchNode, AssessmentModelContentNode, AssessmentModelAssessment, AssessmentModelCollectionResult, AssessmentModelBranchNodeResult, AssessmentModelAsyncActionConfiguration, AssessmentModelKotlinx_serialization_coreEncoder, AssessmentModelKotlinx_serialization_coreSerialDescriptor, AssessmentModelKotlinx_serialization_coreSerializationStrategy, AssessmentModelKotlinx_serialization_coreDecoder, AssessmentModelKotlinx_serialization_coreDeserializationStrategy, AssessmentModelKotlinx_serialization_coreKSerializer, AssessmentModelStringEnumCompanion, AssessmentModelOptionalStep, AssessmentModelActiveStep, AssessmentModelPermissionInfo, AssessmentModelPermissionStep, AssessmentModelNodeContainer, AssessmentModelRecorderConfiguration, AssessmentModelModalViewButtonActionInfo, AssessmentModelChoiceInputItemState, AssessmentModelInputItemState, AssessmentModelFieldState, AssessmentModelQuestionFieldState, AssessmentModelInputItem, AssessmentModelChoiceInputItem, AssessmentModelAnswerResult, AssessmentModelAnyInputItemState, AssessmentModelChoiceOption, AssessmentModelQuestion, AssessmentModelChoiceQuestion, AssessmentModelSurveyRule, AssessmentModelDateTimeFormatOptions, AssessmentModelTextValidator, AssessmentModelKeyboardOptions, AssessmentModelKeyboardTextInputItem, AssessmentModelBranchNodeState, AssessmentModelNodeState, AssessmentModelLeafNodeState, AssessmentModelFormStepState, AssessmentModelFormStep, AssessmentModelInvalidMessage, AssessmentModelKeyboardInputItemState, AssessmentModelSkipCheckboxInputItem, AssessmentModelSkipCheckboxQuestion, AssessmentModelRange, AssessmentModelQuestionState, AssessmentModelSampleRecord, AssessmentModelTableRecorderConfiguration, AssessmentModelNavigationRule, AssessmentModelDirectNavigationRule, AssessmentModelAnimatedImageInfo, AssessmentModelImageTheme, AssessmentModelAssessmentGroupInfo, AssessmentModelAssessmentResult, AssessmentModelAsyncActionContainer, AssessmentModelResultNavigationRule, AssessmentModelCheckboxInputItem, AssessmentModelSurveyNavigationRule, AssessmentModelComboBoxQuestion, AssessmentModelComparableSurveyRule, AssessmentModelCountdownStep, AssessmentModelDateTimeInputItem, AssessmentModelNumberRange, AssessmentModelAssessmentProvider, AssessmentModelResourceAssessmentProvider, AssessmentModelInstructionStep, AssessmentModelMultipleInputQuestion, AssessmentModelNavigationButtonActionInfo, AssessmentModelOverviewStep, AssessmentModelReminderButtonActionInfo, AssessmentModelResultSummaryStep, AssessmentModelSection, AssessmentModelSimpleQuestion, AssessmentModelTransformableNode, AssessmentModelTransformableAssessment, AssessmentModelVideoViewButtonActionInfo, AssessmentModelWebViewButtonActionInfo, AssessmentModelRootNodeController, AssessmentModelKotlinIterator, AssessmentModelKotlinx_serialization_coreSerialFormat, AssessmentModelKotlinx_serialization_coreStringFormat, AssessmentModelKotlinx_serialization_coreCompositeEncoder, AssessmentModelKotlinAnnotation, AssessmentModelKotlinx_serialization_coreCompositeDecoder, AssessmentModelKotlinx_serialization_coreSerializersModuleCollector, AssessmentModelKotlinKClass, AssessmentModelKotlinKDeclarationContainer, AssessmentModelKotlinKAnnotatedElement, AssessmentModelKotlinKClassifier;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

__attribute__((swift_name("KotlinBase")))
@interface AssessmentModelBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end;

@interface AssessmentModelBase (AssessmentModelBaseCopying) <NSCopying>
@end;

__attribute__((swift_name("KotlinMutableSet")))
@interface AssessmentModelMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end;

__attribute__((swift_name("KotlinMutableDictionary")))
@interface AssessmentModelMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end;

@interface NSError (NSErrorAssessmentModelKotlinException)
@property (readonly) id _Nullable kotlinException;
@end;

__attribute__((swift_name("KotlinNumber")))
@interface AssessmentModelNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end;

__attribute__((swift_name("KotlinByte")))
@interface AssessmentModelByte : AssessmentModelNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end;

__attribute__((swift_name("KotlinUByte")))
@interface AssessmentModelUByte : AssessmentModelNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end;

__attribute__((swift_name("KotlinShort")))
@interface AssessmentModelShort : AssessmentModelNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end;

__attribute__((swift_name("KotlinUShort")))
@interface AssessmentModelUShort : AssessmentModelNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end;

__attribute__((swift_name("KotlinInt")))
@interface AssessmentModelInt : AssessmentModelNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end;

__attribute__((swift_name("KotlinUInt")))
@interface AssessmentModelUInt : AssessmentModelNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end;

__attribute__((swift_name("KotlinLong")))
@interface AssessmentModelLong : AssessmentModelNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end;

__attribute__((swift_name("KotlinULong")))
@interface AssessmentModelULong : AssessmentModelNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end;

__attribute__((swift_name("KotlinFloat")))
@interface AssessmentModelFloat : AssessmentModelNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end;

__attribute__((swift_name("KotlinDouble")))
@interface AssessmentModelDouble : AssessmentModelNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end;

__attribute__((swift_name("KotlinBoolean")))
@interface AssessmentModelBoolean : AssessmentModelNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end;

__attribute__((swift_name("ResultMapElement")))
@protocol AssessmentModelResultMapElement
@required
- (id<AssessmentModelResult>)createResult __attribute__((swift_name("createResult()")));
@property (readonly) NSString * _Nullable comment __attribute__((swift_name("comment")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@end;

__attribute__((swift_name("Node")))
@protocol AssessmentModelNode <AssessmentModelResultMapElement>
@required
- (id<AssessmentModelNode>)unpackFileLoader:(id<AssessmentModelFileLoader>)fileLoader resourceInfo:(id<AssessmentModelResourceInfo>)resourceInfo jsonCoder:(AssessmentModelKotlinx_serialization_jsonJson *)jsonCoder __attribute__((swift_name("unpack(fileLoader:resourceInfo:jsonCoder:)")));
@property (readonly) NSDictionary<AssessmentModelButtonAction *, id<AssessmentModelButtonActionInfo>> *buttonMap __attribute__((swift_name("buttonMap")));
@property (readonly) NSArray<AssessmentModelButtonAction *> *hideButtons __attribute__((swift_name("hideButtons")));
@end;

__attribute__((swift_name("Step")))
@protocol AssessmentModelStep <AssessmentModelNode>
@required
@property (readonly) NSDictionary<AssessmentModelSpokenInstructionTiming *, NSString *> * _Nullable spokenInstructions __attribute__((swift_name("spokenInstructions")));
@property (readonly) id<AssessmentModelViewTheme> _Nullable viewTheme __attribute__((swift_name("viewTheme")));
@end;

__attribute__((swift_name("ActiveStep")))
@protocol AssessmentModelActiveStep <AssessmentModelStep>
@required
@property (readonly) NSSet<AssessmentModelActiveStepCommand *> *commands __attribute__((swift_name("commands")));
@property (readonly) double duration __attribute__((swift_name("duration")));
@property (readonly) BOOL requiresBackgroundAudio __attribute__((swift_name("requiresBackgroundAudio")));
@property (readonly) BOOL shouldEndOnInterrupt __attribute__((swift_name("shouldEndOnInterrupt")));
@end;

__attribute__((swift_name("KotlinComparable")))
@protocol AssessmentModelKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end;

__attribute__((swift_name("KotlinEnum")))
@interface AssessmentModelKotlinEnum<E> : AssessmentModelBase <AssessmentModelKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end;

__attribute__((swift_name("StringEnum")))
@protocol AssessmentModelStringEnum
@required
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) NSString * _Nullable serialName __attribute__((swift_name("serialName")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ActiveStepCommand")))
@interface AssessmentModelActiveStepCommand : AssessmentModelKotlinEnum<AssessmentModelActiveStepCommand *> <AssessmentModelStringEnum>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelActiveStepCommand *playsoundonstart __attribute__((swift_name("playsoundonstart")));
@property (class, readonly) AssessmentModelActiveStepCommand *playsoundonfinish __attribute__((swift_name("playsoundonfinish")));
@property (class, readonly) AssessmentModelActiveStepCommand *vibrateonstart __attribute__((swift_name("vibrateonstart")));
@property (class, readonly) AssessmentModelActiveStepCommand *vibrateonfinish __attribute__((swift_name("vibrateonfinish")));
@property (class, readonly) AssessmentModelActiveStepCommand *starttimerautomatically __attribute__((swift_name("starttimerautomatically")));
@property (class, readonly) AssessmentModelActiveStepCommand *continueonfinish __attribute__((swift_name("continueonfinish")));
@property (class, readonly) AssessmentModelActiveStepCommand *shoulddisableidletimer __attribute__((swift_name("shoulddisableidletimer")));
@property (class, readonly) AssessmentModelActiveStepCommand *speakwarningonpause __attribute__((swift_name("speakwarningonpause")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ActiveStepCommand.Companion")))
@interface AssessmentModelActiveStepCommandCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (NSSet<AssessmentModelActiveStepCommand *> *)fromStringsStrings:(NSSet<NSString *> *)strings __attribute__((swift_name("fromStrings(strings:)")));
@end;

__attribute__((swift_name("AssetInfo")))
@protocol AssessmentModelAssetInfo
@required
@property (readonly) NSString * _Nullable rawFileExtension __attribute__((swift_name("rawFileExtension")));
@property (readonly) NSString * _Nullable resourceAssetType __attribute__((swift_name("resourceAssetType")));
@property (readonly) NSString *resourceName __attribute__((swift_name("resourceName")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((swift_name("ResourceInfo")))
@protocol AssessmentModelResourceInfo
@required
@property (readonly) NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property id _Nullable decoderBundle __attribute__((swift_name("decoderBundle")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@end;

__attribute__((swift_name("AssetResourceInfo")))
@protocol AssessmentModelAssetResourceInfo <AssessmentModelAssetInfo, AssessmentModelResourceInfo>
@required
@end;

__attribute__((swift_name("DrawableLayout")))
@protocol AssessmentModelDrawableLayout
@required
@end;

__attribute__((swift_name("ImageInfo")))
@protocol AssessmentModelImageInfo <AssessmentModelAssetResourceInfo, AssessmentModelDrawableLayout>
@required
@property (readonly) NSString *imageName __attribute__((swift_name("imageName")));
@property (readonly) NSString * _Nullable label __attribute__((swift_name("label")));
@end;

__attribute__((swift_name("AnimatedImageInfo")))
@protocol AssessmentModelAnimatedImageInfo <AssessmentModelImageInfo>
@required
@property (readonly) double animationDuration __attribute__((swift_name("animationDuration")));
@property (readonly) AssessmentModelInt * _Nullable animationRepeatCount __attribute__((swift_name("animationRepeatCount")));
@property (readonly) NSArray<NSString *> *imageNames __attribute__((swift_name("imageNames")));
@end;

__attribute__((swift_name("Result")))
@protocol AssessmentModelResult
@required
- (id<AssessmentModelResult>)doCopyResultIdentifier:(NSString *)identifier __attribute__((swift_name("doCopyResult(identifier:)")));
@property NSString * _Nullable endDateString __attribute__((swift_name("endDateString")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property NSString *startDateString __attribute__((swift_name("startDateString")));
@end;

__attribute__((swift_name("AnswerResult")))
@protocol AssessmentModelAnswerResult <AssessmentModelResult>
@required
@property (readonly) AssessmentModelAnswerType * _Nullable answerType __attribute__((swift_name("answerType")));
@property AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable jsonValue __attribute__((swift_name("jsonValue")));
@end;

__attribute__((swift_name("BranchNode")))
@protocol AssessmentModelBranchNode <AssessmentModelNode>
@required
- (id<AssessmentModelNavigator>)getNavigator __attribute__((swift_name("getNavigator()")));
@end;

__attribute__((swift_name("ContentNode")))
@protocol AssessmentModelContentNode <AssessmentModelNode>
@required
@property (readonly) NSString * _Nullable detail __attribute__((swift_name("detail")));
@property (readonly) NSString * _Nullable footnote __attribute__((swift_name("footnote")));
@property (readonly) id<AssessmentModelImageInfo> _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@property (readonly) NSString * _Nullable subtitle __attribute__((swift_name("subtitle")));
@property (readonly) NSString * _Nullable title __attribute__((swift_name("title")));
@end;

__attribute__((swift_name("Assessment")))
@protocol AssessmentModelAssessment <AssessmentModelBranchNode, AssessmentModelContentNode>
@required
@property (readonly) int32_t estimatedMinutes __attribute__((swift_name("estimatedMinutes")));
@property (readonly) NSString * _Nullable schemaIdentifier __attribute__((swift_name("schemaIdentifier")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((swift_name("AssessmentProvider")))
@protocol AssessmentModelAssessmentProvider
@required
- (BOOL)canLoadAssessmentAssessmentIdentifier:(NSString *)assessmentIdentifier __attribute__((swift_name("canLoadAssessment(assessmentIdentifier:)")));
- (id<AssessmentModelAssessment> _Nullable)loadAssessmentAssessmentIdentifier:(NSString *)assessmentIdentifier __attribute__((swift_name("loadAssessment(assessmentIdentifier:)")));
@end;

__attribute__((swift_name("CollectionResult")))
@protocol AssessmentModelCollectionResult <AssessmentModelResult>
@required
@property AssessmentModelMutableSet<id<AssessmentModelResult>> *inputResults __attribute__((swift_name("inputResults")));
@end;

__attribute__((swift_name("BranchNodeResult")))
@protocol AssessmentModelBranchNodeResult <AssessmentModelCollectionResult>
@required
@property (readonly) NSMutableArray<AssessmentModelPathMarker *> *path __attribute__((swift_name("path")));
@property NSMutableArray<id<AssessmentModelResult>> *pathHistoryResults __attribute__((swift_name("pathHistoryResults")));
@end;

__attribute__((swift_name("AssessmentResult")))
@protocol AssessmentModelAssessmentResult <AssessmentModelBranchNodeResult>
@required
@property (readonly) NSString * _Nullable assessmentIdentifier __attribute__((swift_name("assessmentIdentifier")));
@property NSString *runUUIDString __attribute__((swift_name("runUUIDString")));
@property (readonly) NSString * _Nullable schemaIdentifier __attribute__((swift_name("schemaIdentifier")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((swift_name("AsyncActionConfiguration")))
@protocol AssessmentModelAsyncActionConfiguration <AssessmentModelResultMapElement>
@required
@property (readonly) NSString * _Nullable startStepIdentifier __attribute__((swift_name("startStepIdentifier")));
@end;

__attribute__((swift_name("AsyncActionContainer")))
@protocol AssessmentModelAsyncActionContainer <AssessmentModelNode>
@required
@property (readonly) NSArray<id<AssessmentModelAsyncActionConfiguration>> *backgroundActions __attribute__((swift_name("backgroundActions")));
@end;

__attribute__((swift_name("ButtonAction")))
@interface AssessmentModelButtonAction : AssessmentModelBase <AssessmentModelStringEnum>
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerializationStrategy")))
@protocol AssessmentModelKotlinx_serialization_coreSerializationStrategy
@required
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreDeserializationStrategy")))
@protocol AssessmentModelKotlinx_serialization_coreDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreKSerializer")))
@protocol AssessmentModelKotlinx_serialization_coreKSerializer <AssessmentModelKotlinx_serialization_coreSerializationStrategy, AssessmentModelKotlinx_serialization_coreDeserializationStrategy>
@required
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonAction.Companion")))
@interface AssessmentModelButtonActionCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelButtonAction *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelButtonAction *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (AssessmentModelButtonAction *)valueOfName:(NSString *)name __attribute__((swift_name("valueOf(name:)")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonAction.Custom")))
@interface AssessmentModelButtonActionCustom : AssessmentModelButtonAction
- (instancetype)initWithName:(NSString *)name __attribute__((swift_name("init(name:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelButtonActionCustom *)doCopyName:(NSString *)name __attribute__((swift_name("doCopy(name:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((swift_name("ButtonAction.Navigation")))
@interface AssessmentModelButtonActionNavigation : AssessmentModelButtonAction
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonAction.NavigationCancel")))
@interface AssessmentModelButtonActionNavigationCancel : AssessmentModelButtonActionNavigation
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)cancel __attribute__((swift_name("init()")));
@end;

__attribute__((swift_name("StringEnumCompanion")))
@protocol AssessmentModelStringEnumCompanion
@required
- (id<AssessmentModelStringEnum> _Nullable)valueOfName:(NSString *)name __attribute__((swift_name("valueOf(name:)")));
- (AssessmentModelKotlinArray<id<AssessmentModelStringEnum>> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonAction.NavigationCompanion")))
@interface AssessmentModelButtonActionNavigationCompanion : AssessmentModelBase <AssessmentModelStringEnumCompanion>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelKotlinArray<AssessmentModelButtonActionNavigation *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonAction.NavigationGoBackward")))
@interface AssessmentModelButtonActionNavigationGoBackward : AssessmentModelButtonActionNavigation
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)goBackward __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonAction.NavigationGoForward")))
@interface AssessmentModelButtonActionNavigationGoForward : AssessmentModelButtonActionNavigation
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)goForward __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonAction.NavigationLearnMore")))
@interface AssessmentModelButtonActionNavigationLearnMore : AssessmentModelButtonActionNavigation
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)learnMore __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonAction.NavigationReviewInstructions")))
@interface AssessmentModelButtonActionNavigationReviewInstructions : AssessmentModelButtonActionNavigation
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)reviewInstructions __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonAction.NavigationSkip")))
@interface AssessmentModelButtonActionNavigationSkip : AssessmentModelButtonActionNavigation
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)skip __attribute__((swift_name("init()")));
@end;

__attribute__((swift_name("ButtonActionInfo")))
@protocol AssessmentModelButtonActionInfo
@required
@property (readonly) NSString * _Nullable buttonTitle __attribute__((swift_name("buttonTitle")));
@property (readonly) id<AssessmentModelImageInfo> _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@end;

__attribute__((swift_name("ButtonStyle")))
@interface AssessmentModelButtonStyle : AssessmentModelBase
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonStyle.Companion")))
@interface AssessmentModelButtonStyleCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonStyle.Footer")))
@interface AssessmentModelButtonStyleFooter : AssessmentModelButtonStyle
- (instancetype)initWithButtonTitle:(NSString *)buttonTitle __attribute__((swift_name("init(buttonTitle:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelButtonStyleFooter *)doCopyButtonTitle:(NSString *)buttonTitle __attribute__((swift_name("doCopy(buttonTitle:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *buttonTitle __attribute__((swift_name("buttonTitle")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonStyle.FooterCompanion")))
@interface AssessmentModelButtonStyleFooterCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("ButtonStyle.NavigationHeader")))
@interface AssessmentModelButtonStyleNavigationHeader : AssessmentModelButtonStyle
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonStyle.NavigationHeaderBack")))
@interface AssessmentModelButtonStyleNavigationHeaderBack : AssessmentModelButtonStyleNavigationHeader
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)back __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AssessmentModelKotlinArray<id<AssessmentModelKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonStyle.NavigationHeaderClose")))
@interface AssessmentModelButtonStyleNavigationHeaderClose : AssessmentModelButtonStyleNavigationHeader
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)close __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AssessmentModelKotlinArray<id<AssessmentModelKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonStyle.NavigationHeaderCompanion")))
@interface AssessmentModelButtonStyleNavigationHeaderCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("OptionalStep")))
@protocol AssessmentModelOptionalStep <AssessmentModelStep>
@required
@property (readonly) BOOL fullInstructionsOnly __attribute__((swift_name("fullInstructionsOnly")));
@end;

__attribute__((swift_name("CountdownStep")))
@protocol AssessmentModelCountdownStep <AssessmentModelOptionalStep, AssessmentModelActiveStep>
@required
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateGenerator")))
@interface AssessmentModelDateGenerator : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)dateGenerator __attribute__((swift_name("init()")));
- (int32_t)currentYear __attribute__((swift_name("currentYear()")));
- (NSString *)nowString __attribute__((swift_name("nowString()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Factory")))
@interface AssessmentModelFactory : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)factory __attribute__((swift_name("init()")));
- (AssessmentModelProduct *)createConfig:(NSDictionary<NSString *, NSString *> *)config __attribute__((swift_name("create(config:)")));
@property (readonly) NSString *platform __attribute__((swift_name("platform")));
@end;

__attribute__((swift_name("FormStep")))
@protocol AssessmentModelFormStep <AssessmentModelStep, AssessmentModelContentNode>
@required
@property (readonly) NSArray<id<AssessmentModelNode>> *children __attribute__((swift_name("children")));
@end;

__attribute__((swift_name("InstructionStep")))
@protocol AssessmentModelInstructionStep <AssessmentModelOptionalStep, AssessmentModelContentNode>
@required
@end;

__attribute__((swift_name("ModalViewButtonActionInfo")))
@protocol AssessmentModelModalViewButtonActionInfo <AssessmentModelButtonActionInfo, AssessmentModelResourceInfo>
@required
@property (readonly) AssessmentModelButtonStyle *backButtonStyle __attribute__((swift_name("backButtonStyle")));
@property (readonly) NSString * _Nullable title __attribute__((swift_name("title")));
@end;

__attribute__((swift_name("NavigationButtonActionInfo")))
@protocol AssessmentModelNavigationButtonActionInfo <AssessmentModelButtonActionInfo>
@required
@property (readonly) NSString *skipToIdentifier __attribute__((swift_name("skipToIdentifier")));
@end;

__attribute__((swift_name("NodeContainer")))
@protocol AssessmentModelNodeContainer <AssessmentModelBranchNode>
@required
@property (readonly) NSArray<id<AssessmentModelNode>> *children __attribute__((swift_name("children")));
@property (readonly) NSArray<NSString *> * _Nullable progressMarkers __attribute__((swift_name("progressMarkers")));
@end;

__attribute__((swift_name("PermissionStep")))
@protocol AssessmentModelPermissionStep <AssessmentModelStep, AssessmentModelContentNode>
@required
@property (readonly) NSArray<id<AssessmentModelPermissionInfo>> * _Nullable permissions __attribute__((swift_name("permissions")));
@end;

__attribute__((swift_name("OverviewStep")))
@protocol AssessmentModelOverviewStep <AssessmentModelPermissionStep>
@required
- (void)setDetail:(NSString * _Nullable)value __attribute__((swift_name("setDetail(_:)")));
@property (readonly) NSArray<id<AssessmentModelImageInfo>> * _Nullable icons __attribute__((swift_name("icons")));
@property id<AssessmentModelButtonActionInfo> _Nullable learnMore __attribute__((swift_name("learnMore")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PathMarker")))
@interface AssessmentModelPathMarker : AssessmentModelBase
- (instancetype)initWithIdentifier:(NSString *)identifier direction:(AssessmentModelNavigationPointDirection *)direction __attribute__((swift_name("init(identifier:direction:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelNavigationPointDirection *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelPathMarker *)doCopyIdentifier:(NSString *)identifier direction:(AssessmentModelNavigationPointDirection *)direction __attribute__((swift_name("doCopy(identifier:direction:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelNavigationPointDirection *direction __attribute__((swift_name("direction")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PathMarker.Companion")))
@interface AssessmentModelPathMarkerCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("PermissionInfo")))
@protocol AssessmentModelPermissionInfo
@required
@property (readonly) NSString * _Nullable deniedMessage __attribute__((swift_name("deniedMessage")));
@property (readonly) BOOL optional __attribute__((swift_name("optional")));
@property (readonly) AssessmentModelPermissionType *permissionType __attribute__((swift_name("permissionType")));
@property (readonly) NSString * _Nullable reason __attribute__((swift_name("reason")));
@property (readonly) BOOL requiresBackground __attribute__((swift_name("requiresBackground")));
@property (readonly) NSString * _Nullable restrictedMessage __attribute__((swift_name("restrictedMessage")));
@end;

__attribute__((swift_name("PermissionType")))
@interface AssessmentModelPermissionType : AssessmentModelBase <AssessmentModelStringEnum>
- (AssessmentModelPermissionInfoObject *)createPermissionInfoOptional:(BOOL)optional requiresBackground:(BOOL)requiresBackground reason:(NSString * _Nullable)reason __attribute__((swift_name("createPermissionInfo(optional:requiresBackground:reason:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PermissionType.Companion")))
@interface AssessmentModelPermissionTypeCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelPermissionType *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelPermissionType *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (AssessmentModelPermissionType *)valueOfName:(NSString *)name __attribute__((swift_name("valueOf(name:)")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PermissionType.Custom")))
@interface AssessmentModelPermissionTypeCustom : AssessmentModelPermissionType
- (instancetype)initWithName:(NSString *)name __attribute__((swift_name("init(name:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelPermissionTypeCustom *)doCopyName:(NSString *)name __attribute__((swift_name("doCopy(name:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((swift_name("PermissionType.Standard")))
@interface AssessmentModelPermissionTypeStandard : AssessmentModelPermissionType
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PermissionType.StandardCamera")))
@interface AssessmentModelPermissionTypeStandardCamera : AssessmentModelPermissionTypeStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)camera __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PermissionType.StandardCompanion")))
@interface AssessmentModelPermissionTypeStandardCompanion : AssessmentModelBase <AssessmentModelStringEnumCompanion>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelKotlinArray<AssessmentModelPermissionTypeStandard *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PermissionType.StandardLocation")))
@interface AssessmentModelPermissionTypeStandardLocation : AssessmentModelPermissionTypeStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)location __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PermissionType.StandardMicrophone")))
@interface AssessmentModelPermissionTypeStandardMicrophone : AssessmentModelPermissionTypeStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)microphone __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PermissionType.StandardMotion")))
@interface AssessmentModelPermissionTypeStandardMotion : AssessmentModelPermissionTypeStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)motion __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PermissionType.StandardPhotoLibrary")))
@interface AssessmentModelPermissionTypeStandardPhotoLibrary : AssessmentModelPermissionTypeStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)photoLibrary __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Platform")))
@interface AssessmentModelPlatform : AssessmentModelBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@property (readonly) NSString *platform __attribute__((swift_name("platform")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Product")))
@interface AssessmentModelProduct : AssessmentModelBase
- (instancetype)initWithUser:(NSString *)user __attribute__((swift_name("init(user:)"))) __attribute__((objc_designated_initializer));
- (void)iosSpecificOperation __attribute__((swift_name("iosSpecificOperation()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *model __attribute__((swift_name("model")));
@property (readonly) NSString *user __attribute__((swift_name("user")));
@end;

__attribute__((swift_name("RecorderConfiguration")))
@protocol AssessmentModelRecorderConfiguration <AssessmentModelAsyncActionConfiguration>
@required
@property (readonly) BOOL optional __attribute__((swift_name("optional")));
@property (readonly) NSArray<id<AssessmentModelPermissionInfo>> * _Nullable permissions __attribute__((swift_name("permissions")));
@property (readonly) NSString * _Nullable reason __attribute__((swift_name("reason")));
@property (readonly) BOOL requiresBackground __attribute__((swift_name("requiresBackground")));
@property (readonly) BOOL shouldDeletePrevious __attribute__((swift_name("shouldDeletePrevious")));
@property (readonly) NSString * _Nullable stopStepIdentifier __attribute__((swift_name("stopStepIdentifier")));
@end;

__attribute__((swift_name("ReminderButtonActionInfo")))
@protocol AssessmentModelReminderButtonActionInfo <AssessmentModelButtonActionInfo>
@required
@property (readonly) NSString * _Nullable reminderAlert __attribute__((swift_name("reminderAlert")));
@property (readonly) NSString *reminderIdentifier __attribute__((swift_name("reminderIdentifier")));
@property (readonly) NSString * _Nullable reminderPrompt __attribute__((swift_name("reminderPrompt")));
@end;

__attribute__((swift_name("ResultSummaryStep")))
@protocol AssessmentModelResultSummaryStep <AssessmentModelStep, AssessmentModelContentNode>
@required
@property (readonly) NSString * _Nullable resultTitle __attribute__((swift_name("resultTitle")));
@property (readonly) AssessmentModelIdentifierPath * _Nullable scoringResultPath __attribute__((swift_name("scoringResultPath")));
@end;

__attribute__((swift_name("SampleRecord")))
@protocol AssessmentModelSampleRecord
@required
@property (readonly) NSString * _Nullable stepPath __attribute__((swift_name("stepPath")));
@property (readonly) AssessmentModelDouble * _Nullable timestamp __attribute__((swift_name("timestamp")));
@property (readonly) NSString * _Nullable timestampDateString __attribute__((swift_name("timestampDateString")));
@end;

__attribute__((swift_name("Section")))
@protocol AssessmentModelSection <AssessmentModelNodeContainer, AssessmentModelContentNode>
@required
@end;

__attribute__((swift_name("SpokenInstructionTiming")))
@interface AssessmentModelSpokenInstructionTiming : AssessmentModelBase <AssessmentModelStringEnum>
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SpokenInstructionTiming.Companion")))
@interface AssessmentModelSpokenInstructionTimingCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelSpokenInstructionTiming *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelSpokenInstructionTiming *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("SpokenInstructionTiming.Keyword")))
@interface AssessmentModelSpokenInstructionTimingKeyword : AssessmentModelSpokenInstructionTiming
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SpokenInstructionTiming.KeywordCompanion")))
@interface AssessmentModelSpokenInstructionTimingKeywordCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelKotlinArray<AssessmentModelSpokenInstructionTimingKeyword *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SpokenInstructionTiming.KeywordCountdown")))
@interface AssessmentModelSpokenInstructionTimingKeywordCountdown : AssessmentModelSpokenInstructionTimingKeyword
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)countdown __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SpokenInstructionTiming.KeywordEnd")))
@interface AssessmentModelSpokenInstructionTimingKeywordEnd : AssessmentModelSpokenInstructionTimingKeyword
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)end __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SpokenInstructionTiming.KeywordHalfway")))
@interface AssessmentModelSpokenInstructionTimingKeywordHalfway : AssessmentModelSpokenInstructionTimingKeyword
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)halfway __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SpokenInstructionTiming.KeywordStart")))
@interface AssessmentModelSpokenInstructionTimingKeywordStart : AssessmentModelSpokenInstructionTimingKeyword
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)start __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SpokenInstructionTiming.TimeInterval")))
@interface AssessmentModelSpokenInstructionTimingTimeInterval : AssessmentModelSpokenInstructionTiming
- (instancetype)initWithTime:(double)time __attribute__((swift_name("init(time:)"))) __attribute__((objc_designated_initializer));
- (double)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelSpokenInstructionTimingTimeInterval *)doCopyTime:(double)time __attribute__((swift_name("doCopy(time:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) double time __attribute__((swift_name("time")));
@end;

__attribute__((swift_name("TableRecorderConfiguration")))
@protocol AssessmentModelTableRecorderConfiguration <AssessmentModelRecorderConfiguration>
@required
@property (readonly) BOOL usesCSVEncoding __attribute__((swift_name("usesCSVEncoding")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UUIDGenerator")))
@interface AssessmentModelUUIDGenerator : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)uUIDGenerator __attribute__((swift_name("init()")));
- (NSString *)uuidString __attribute__((swift_name("uuidString()")));
@end;

__attribute__((swift_name("VideoViewButtonActionInfo")))
@protocol AssessmentModelVideoViewButtonActionInfo <AssessmentModelModalViewButtonActionInfo>
@required
@property (readonly) NSString *url __attribute__((swift_name("url")));
@end;

__attribute__((swift_name("ViewTheme")))
@protocol AssessmentModelViewTheme <AssessmentModelResourceInfo>
@required
@property (readonly) NSString * _Nullable fragmentIdentifier __attribute__((swift_name("fragmentIdentifier")));
@property (readonly) NSString * _Nullable fragmentLayout __attribute__((swift_name("fragmentLayout")));
@property (readonly) NSString * _Nullable storyboardIdentifier __attribute__((swift_name("storyboardIdentifier")));
@property (readonly) NSString * _Nullable viewIdentifier __attribute__((swift_name("viewIdentifier")));
@end;

__attribute__((swift_name("WebViewButtonActionInfo")))
@protocol AssessmentModelWebViewButtonActionInfo <AssessmentModelModalViewButtonActionInfo>
@required
@property (readonly) NSString *url __attribute__((swift_name("url")));
@end;

__attribute__((swift_name("FieldState")))
@protocol AssessmentModelFieldState
@required
@property (readonly) id<AssessmentModelResult> currentResult __attribute__((swift_name("currentResult")));
@property (readonly) int32_t index __attribute__((swift_name("index")));
@property (readonly) id<AssessmentModelNode> node __attribute__((swift_name("node")));
@end;

__attribute__((swift_name("QuestionFieldState")))
@protocol AssessmentModelQuestionFieldState <AssessmentModelFieldState>
@required
- (BOOL)allAnswersValid __attribute__((swift_name("allAnswersValid()")));
- (BOOL)didChangeSelectionStateSelected:(BOOL)selected forItem:(id<AssessmentModelChoiceInputItemState>)forItem __attribute__((swift_name("didChangeSelectionState(selected:forItem:)")));
- (BOOL)saveAnswerAnswer:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)answer forItem:(id<AssessmentModelInputItemState>)forItem __attribute__((swift_name("saveAnswer(answer:forItem:)")));
@property (readonly) AssessmentModelAnswerType *answerType __attribute__((swift_name("answerType")));
@property (readonly) NSArray<id<AssessmentModelInputItemState>> *itemStates __attribute__((swift_name("itemStates")));
@end;

__attribute__((swift_name("AbstractQuestionFieldStateImpl")))
@interface AssessmentModelAbstractQuestionFieldStateImpl : AssessmentModelBase <AssessmentModelQuestionFieldState>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (BOOL)allAnswersValid __attribute__((swift_name("allAnswersValid()")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)answerForIndex:(int32_t)index inputItem:(id<AssessmentModelInputItem>)inputItem __attribute__((swift_name("answerFor(index:inputItem:)")));
- (BOOL)didChangeSelectionStateSelected:(BOOL)selected forItem:(id<AssessmentModelChoiceInputItemState>)forItem __attribute__((swift_name("didChangeSelectionState(selected:forItem:)")));
- (id<AssessmentModelInputItemState>)itemStateForIndex:(int32_t)index inputItem:(id<AssessmentModelInputItem>)inputItem __attribute__((swift_name("itemStateFor(index:inputItem:)")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValueForMap:(NSDictionary<NSString *, AssessmentModelKotlinx_serialization_jsonJsonElement *> *)forMap __attribute__((swift_name("jsonValue(forMap:)")));
- (BOOL)saveAnswerAnswer:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)answer forItem:(id<AssessmentModelInputItemState>)forItem __attribute__((swift_name("saveAnswer(answer:forItem:)")));
- (BOOL)selectedForIndex:(int32_t)index choice:(id<AssessmentModelChoiceInputItem>)choice __attribute__((swift_name("selectedFor(index:choice:)")));
- (BOOL)updateAnswerStateChangedItem:(id<AssessmentModelInputItemState>)changedItem __attribute__((swift_name("updateAnswerState(changedItem:)")));
@property (readonly) AssessmentModelAnswerType *answerType __attribute__((swift_name("answerType")));
@property (readonly) NSArray<id<AssessmentModelInputItemState>> *itemStates __attribute__((swift_name("itemStates")));
@end;

__attribute__((swift_name("AnswerType")))
@interface AssessmentModelAnswerType : AssessmentModelBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (id<AssessmentModelAnswerResult>)createAnswerResultIdentifier:(NSString *)identifier value:(id)value __attribute__((swift_name("createAnswerResult(identifier:value:)")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement *)jsonElementForValue:(id)value __attribute__((swift_name("jsonElementFor(value:)")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@property (readonly) AssessmentModelKotlinx_serialization_coreSerialKind *serialKind __attribute__((swift_name("serialKind")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.Array")))
@interface AssessmentModelAnswerTypeArray : AssessmentModelAnswerType
- (instancetype)initWithBaseType:(AssessmentModelBaseType *)baseType sequenceSeparator:(NSString * _Nullable)sequenceSeparator __attribute__((swift_name("init(baseType:sequenceSeparator:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (AssessmentModelBaseType *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelAnswerTypeArray *)doCopyBaseType:(AssessmentModelBaseType *)baseType sequenceSeparator:(NSString * _Nullable)sequenceSeparator __attribute__((swift_name("doCopy(baseType:sequenceSeparator:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement *)jsonElementForValue:(id)value __attribute__((swift_name("jsonElementFor(value:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@property (readonly) NSString * _Nullable sequenceSeparator __attribute__((swift_name("sequenceSeparator")));
@property (readonly) AssessmentModelKotlinx_serialization_coreSerialKind *serialKind __attribute__((swift_name("serialKind")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.ArrayCompanion")))
@interface AssessmentModelAnswerTypeArrayCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.BOOLEAN")))
@interface AssessmentModelAnswerTypeBOOLEAN : AssessmentModelAnswerType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)bOOLEAN __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AssessmentModelKotlinArray<id<AssessmentModelKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.Companion")))
@interface AssessmentModelAnswerTypeCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (AssessmentModelAnswerType *)valueForBaseType:(AssessmentModelBaseType *)baseType __attribute__((swift_name("valueFor(baseType:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.DECIMAL")))
@interface AssessmentModelAnswerTypeDECIMAL : AssessmentModelAnswerType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)dECIMAL __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AssessmentModelKotlinArray<id<AssessmentModelKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.DateTime")))
@interface AssessmentModelAnswerTypeDateTime : AssessmentModelAnswerType
- (instancetype)initWithCodingFormat:(NSString *)codingFormat __attribute__((swift_name("init(codingFormat:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelAnswerTypeDateTime *)doCopyCodingFormat:(NSString *)codingFormat __attribute__((swift_name("doCopy(codingFormat:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@property (readonly) NSString *codingFormat __attribute__((swift_name("codingFormat")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.DateTimeCompanion")))
@interface AssessmentModelAnswerTypeDateTimeCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.INTEGER")))
@interface AssessmentModelAnswerTypeINTEGER : AssessmentModelAnswerType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)iNTEGER __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AssessmentModelKotlinArray<id<AssessmentModelKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.Measurement")))
@interface AssessmentModelAnswerTypeMeasurement : AssessmentModelAnswerType
- (instancetype)initWithUnit:(NSString * _Nullable)unit __attribute__((swift_name("init(unit:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelAnswerTypeMeasurement *)doCopyUnit:(NSString * _Nullable)unit __attribute__((swift_name("doCopy(unit:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@property (readonly) NSString * _Nullable unit __attribute__((swift_name("unit")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.MeasurementCompanion")))
@interface AssessmentModelAnswerTypeMeasurementCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.NULL")))
@interface AssessmentModelAnswerTypeNULL : AssessmentModelAnswerType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)nULL __attribute__((swift_name("init()")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement *)jsonElementForValue:(id)value __attribute__((swift_name("jsonElementFor(value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AssessmentModelKotlinArray<id<AssessmentModelKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.OBJECT")))
@interface AssessmentModelAnswerTypeOBJECT : AssessmentModelAnswerType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)oBJECT __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AssessmentModelKotlinArray<id<AssessmentModelKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerType.STRING")))
@interface AssessmentModelAnswerTypeSTRING : AssessmentModelAnswerType
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (instancetype)sTRING __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AssessmentModelKotlinArray<id<AssessmentModelKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@end;

__attribute__((swift_name("InputItemState")))
@protocol AssessmentModelInputItemState
@required
@property AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable currentAnswer __attribute__((swift_name("currentAnswer")));
@property (readonly) int32_t index __attribute__((swift_name("index")));
@property (readonly) id<AssessmentModelInputItem> inputItem __attribute__((swift_name("inputItem")));
@property (readonly) NSString *itemIdentifier __attribute__((swift_name("itemIdentifier")));
@property BOOL selected __attribute__((swift_name("selected")));
@property (readonly) NSString *viewIdentifier __attribute__((swift_name("viewIdentifier")));
@end;

__attribute__((swift_name("AnyInputItemState")))
@protocol AssessmentModelAnyInputItemState <AssessmentModelInputItemState>
@required
@property AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable storedAnswer __attribute__((swift_name("storedAnswer")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnyInputItemStateImpl")))
@interface AssessmentModelAnyInputItemStateImpl : AssessmentModelBase <AssessmentModelAnyInputItemState>
- (instancetype)initWithIndex:(int32_t)index inputItem:(id<AssessmentModelInputItem>)inputItem storedAnswer:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)storedAnswer __attribute__((swift_name("init(index:inputItem:storedAnswer:)"))) __attribute__((objc_designated_initializer));
@property (readonly) int32_t index __attribute__((swift_name("index")));
@property (readonly) id<AssessmentModelInputItem> inputItem __attribute__((swift_name("inputItem")));
@property BOOL selected __attribute__((swift_name("selected")));
@property AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable storedAnswer __attribute__((swift_name("storedAnswer")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnyNodeFieldStateImpl")))
@interface AssessmentModelAnyNodeFieldStateImpl : AssessmentModelBase <AssessmentModelFieldState>
- (instancetype)initWithIndex:(int32_t)index node:(id<AssessmentModelNode>)node currentResult:(id<AssessmentModelResult>)currentResult __attribute__((swift_name("init(index:node:currentResult:)"))) __attribute__((objc_designated_initializer));
- (int32_t)component1 __attribute__((swift_name("component1()")));
- (id<AssessmentModelNode>)component2 __attribute__((swift_name("component2()")));
- (id<AssessmentModelResult>)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelAnyNodeFieldStateImpl *)doCopyIndex:(int32_t)index node:(id<AssessmentModelNode>)node currentResult:(id<AssessmentModelResult>)currentResult __attribute__((swift_name("doCopy(index:node:currentResult:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<AssessmentModelResult> currentResult __attribute__((swift_name("currentResult")));
@property (readonly) int32_t index __attribute__((swift_name("index")));
@property (readonly) id<AssessmentModelNode> node __attribute__((swift_name("node")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AutoCapitalizationType")))
@interface AssessmentModelAutoCapitalizationType : AssessmentModelKotlinEnum<AssessmentModelAutoCapitalizationType *> <AssessmentModelStringEnum>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelAutoCapitalizationType *none __attribute__((swift_name("none")));
@property (class, readonly) AssessmentModelAutoCapitalizationType *words __attribute__((swift_name("words")));
@property (class, readonly) AssessmentModelAutoCapitalizationType *sentences __attribute__((swift_name("sentences")));
@property (class, readonly) AssessmentModelAutoCapitalizationType *allcharacters __attribute__((swift_name("allcharacters")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AutoCapitalizationType.Companion")))
@interface AssessmentModelAutoCapitalizationTypeCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelAutoCapitalizationType *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelAutoCapitalizationType *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AutoCorrectionType")))
@interface AssessmentModelAutoCorrectionType : AssessmentModelKotlinEnum<AssessmentModelAutoCorrectionType *> <AssessmentModelStringEnum>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelAutoCorrectionType *default_ __attribute__((swift_name("default_")));
@property (class, readonly) AssessmentModelAutoCorrectionType *no __attribute__((swift_name("no")));
@property (class, readonly) AssessmentModelAutoCorrectionType *yes __attribute__((swift_name("yes")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AutoCorrectionType.Companion")))
@interface AssessmentModelAutoCorrectionTypeCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelAutoCorrectionType *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelAutoCorrectionType *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BaseType")))
@interface AssessmentModelBaseType : AssessmentModelKotlinEnum<AssessmentModelBaseType *> <AssessmentModelStringEnum>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelBaseType *boolean __attribute__((swift_name("boolean")));
@property (class, readonly) AssessmentModelBaseType *number __attribute__((swift_name("number")));
@property (class, readonly) AssessmentModelBaseType *integer __attribute__((swift_name("integer")));
@property (class, readonly) AssessmentModelBaseType *string __attribute__((swift_name("string")));
@property (class, readonly) AssessmentModelBaseType *array __attribute__((swift_name("array")));
@property (class, readonly) AssessmentModelBaseType *object __attribute__((swift_name("object")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement *)jsonElementForValue:(id)value __attribute__((swift_name("jsonElementFor(value:)")));
@property (readonly) AssessmentModelKotlinx_serialization_coreSerialKind *serialKind __attribute__((swift_name("serialKind")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BaseType.Companion")))
@interface AssessmentModelBaseTypeCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelBaseType *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelBaseType *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("InputItem")))
@protocol AssessmentModelInputItem
@required
@property (readonly) AssessmentModelAnswerType *answerType __attribute__((swift_name("answerType")));
@property (readonly) BOOL exclusive __attribute__((swift_name("exclusive")));
@property (readonly) NSString * _Nullable fieldLabel __attribute__((swift_name("fieldLabel")));
@property (readonly) BOOL optional __attribute__((swift_name("optional")));
@property (readonly) NSString * _Nullable placeholder __attribute__((swift_name("placeholder")));
@property (readonly) NSString * _Nullable resultIdentifier __attribute__((swift_name("resultIdentifier")));
@property (readonly) AssessmentModelUIHint *uiHint __attribute__((swift_name("uiHint")));
@end;

__attribute__((swift_name("ChoiceOption")))
@protocol AssessmentModelChoiceOption
@required
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValueSelected:(BOOL)selected __attribute__((swift_name("jsonValue(selected:)")));
@property (readonly) NSString * _Nullable detail __attribute__((swift_name("detail")));
@property (readonly) BOOL exclusive __attribute__((swift_name("exclusive")));
@property (readonly) NSString * _Nullable fieldLabel __attribute__((swift_name("fieldLabel")));
@property (readonly) AssessmentModelFetchableImage * _Nullable icon __attribute__((swift_name("icon")));
@end;

__attribute__((swift_name("ChoiceInputItem")))
@protocol AssessmentModelChoiceInputItem <AssessmentModelInputItem, AssessmentModelChoiceOption>
@required
@end;

__attribute__((swift_name("CheckboxInputItem")))
@protocol AssessmentModelCheckboxInputItem <AssessmentModelChoiceInputItem>
@required
@end;

__attribute__((swift_name("ChoiceInputItemState")))
@protocol AssessmentModelChoiceInputItemState <AssessmentModelInputItemState>
@required
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ChoiceInputItemStateImpl")))
@interface AssessmentModelChoiceInputItemStateImpl : AssessmentModelBase <AssessmentModelChoiceInputItemState>
- (instancetype)initWithIndex:(int32_t)index inputItem:(id<AssessmentModelChoiceInputItem>)inputItem selected:(BOOL)selected __attribute__((swift_name("init(index:inputItem:selected:)"))) __attribute__((objc_designated_initializer));
@property (readonly) int32_t index __attribute__((swift_name("index")));
@property (readonly) id<AssessmentModelChoiceInputItem> inputItem __attribute__((swift_name("inputItem")));
@property BOOL selected __attribute__((swift_name("selected")));
@end;

__attribute__((swift_name("Question")))
@protocol AssessmentModelQuestion <AssessmentModelContentNode>
@required
- (NSArray<id<AssessmentModelInputItem>> *)buildInputItems __attribute__((swift_name("buildInputItems()")));
@property (readonly) AssessmentModelAnswerType *answerType __attribute__((swift_name("answerType")));
@property (readonly) BOOL optional __attribute__((swift_name("optional")));
@property (readonly) BOOL singleAnswer __attribute__((swift_name("singleAnswer")));
@end;

__attribute__((swift_name("ChoiceQuestion")))
@protocol AssessmentModelChoiceQuestion <AssessmentModelQuestion>
@required
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@property (readonly) NSArray<id<AssessmentModelChoiceOption>> *choices __attribute__((swift_name("choices")));
@property AssessmentModelUIHint *uiHint __attribute__((swift_name("uiHint")));
@end;

__attribute__((swift_name("ComboBoxQuestion")))
@protocol AssessmentModelComboBoxQuestion <AssessmentModelChoiceQuestion>
@required
@property (readonly) id<AssessmentModelInputItem> otherInputItem __attribute__((swift_name("otherInputItem")));
@end;

__attribute__((swift_name("SurveyRule")))
@protocol AssessmentModelSurveyRule
@required
- (NSString * _Nullable)evaluateRuleWithResult:(id<AssessmentModelResult> _Nullable)result __attribute__((swift_name("evaluateRuleWith(result:)")));
@end;

__attribute__((swift_name("ComparableSurveyRule")))
@protocol AssessmentModelComparableSurveyRule <AssessmentModelSurveyRule>
@required
@property (readonly) double accuracy __attribute__((swift_name("accuracy")));
@property (readonly) AssessmentModelKotlinx_serialization_jsonJsonElement *matchingAnswer __attribute__((swift_name("matchingAnswer")));
@property (readonly) AssessmentModelSurveyRuleOperator * _Nullable ruleOperator __attribute__((swift_name("ruleOperator")));
@property (readonly) NSString * _Nullable skipToIdentifier __attribute__((swift_name("skipToIdentifier")));
@end;

__attribute__((swift_name("DateTimePart")))
@interface AssessmentModelDateTimePart : AssessmentModelBase
@property (readonly) NSString *isoKey __attribute__((swift_name("isoKey")));
@end;

__attribute__((swift_name("DatePart")))
@interface AssessmentModelDatePart : AssessmentModelDateTimePart
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DatePart.Companion")))
@interface AssessmentModelDatePartCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (NSArray<AssessmentModelDatePart *> *)values __attribute__((swift_name("values()")));
@property (readonly) NSString *separator __attribute__((swift_name("separator")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DatePart.Day")))
@interface AssessmentModelDatePartDay : AssessmentModelDatePart
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)day __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DatePart.Month")))
@interface AssessmentModelDatePartMonth : AssessmentModelDatePart
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)month __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DatePart.Year")))
@interface AssessmentModelDatePartYear : AssessmentModelDatePart
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)year __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateTime")))
@interface AssessmentModelDateTime : AssessmentModelBase <AssessmentModelKotlinComparable>
- (instancetype)initWithDateTimeReference:(AssessmentModelDateTimeReference *)dateTimeReference __attribute__((swift_name("init(dateTimeReference:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithIso8601Format:(NSString *)iso8601Format includeTimeZoneIdentifier:(BOOL)includeTimeZoneIdentifier __attribute__((swift_name("init(iso8601Format:includeTimeZoneIdentifier:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithParts:(NSArray<AssessmentModelDateTimePart *> *)parts __attribute__((swift_name("init(parts:)"))) __attribute__((objc_designated_initializer));
- (int32_t)compareToOther:(AssessmentModelDateTime *)other __attribute__((swift_name("compareTo(other:)")));
- (AssessmentModelDateTimeComponents *)getDateTimeComponents __attribute__((swift_name("getDateTimeComponents()")));
- (NSArray<AssessmentModelDateTimePart *> *)getDateTimeParts __attribute__((swift_name("getDateTimeParts()")));
- (AssessmentModelDateTimeReference *)getDateTimeReference __attribute__((swift_name("getDateTimeReference()")));
@property NSDate *dateBehind __attribute__((swift_name("dateBehind")));
@property (readonly) NSString *iso8601Format __attribute__((swift_name("iso8601Format")));
@property (readonly) NSTimeZone * _Nullable timeZone __attribute__((swift_name("timeZone")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateTimeComponents")))
@interface AssessmentModelDateTimeComponents : AssessmentModelBase
- (instancetype)initWithYear:(AssessmentModelInt * _Nullable)year month:(AssessmentModelInt * _Nullable)month day:(AssessmentModelInt * _Nullable)day hour:(AssessmentModelInt * _Nullable)hour minute:(AssessmentModelInt * _Nullable)minute second:(AssessmentModelInt * _Nullable)second gmtHourOffset:(AssessmentModelDouble * _Nullable)gmtHourOffset timezoneIdentifier:(NSString * _Nullable)timezoneIdentifier __attribute__((swift_name("init(year:month:day:hour:minute:second:gmtHourOffset:timezoneIdentifier:)"))) __attribute__((objc_designated_initializer));
- (AssessmentModelInt * _Nullable)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelInt * _Nullable)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelInt * _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelInt * _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelInt * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelInt * _Nullable)component6 __attribute__((swift_name("component6()")));
- (AssessmentModelDouble * _Nullable)component7 __attribute__((swift_name("component7()")));
- (NSString * _Nullable)component8 __attribute__((swift_name("component8()")));
- (AssessmentModelDateTimeComponents *)doCopyYear:(AssessmentModelInt * _Nullable)year month:(AssessmentModelInt * _Nullable)month day:(AssessmentModelInt * _Nullable)day hour:(AssessmentModelInt * _Nullable)hour minute:(AssessmentModelInt * _Nullable)minute second:(AssessmentModelInt * _Nullable)second gmtHourOffset:(AssessmentModelDouble * _Nullable)gmtHourOffset timezoneIdentifier:(NSString * _Nullable)timezoneIdentifier __attribute__((swift_name("doCopy(year:month:day:hour:minute:second:gmtHourOffset:timezoneIdentifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelInt * _Nullable day __attribute__((swift_name("day")));
@property (readonly) AssessmentModelDouble * _Nullable gmtHourOffset __attribute__((swift_name("gmtHourOffset")));
@property (readonly) AssessmentModelInt * _Nullable hour __attribute__((swift_name("hour")));
@property (readonly) AssessmentModelInt * _Nullable minute __attribute__((swift_name("minute")));
@property (readonly) AssessmentModelInt * _Nullable month __attribute__((swift_name("month")));
@property (readonly) AssessmentModelInt * _Nullable second __attribute__((swift_name("second")));
@property (readonly) NSString * _Nullable timezoneIdentifier __attribute__((swift_name("timezoneIdentifier")));
@property (readonly) AssessmentModelInt * _Nullable year __attribute__((swift_name("year")));
@end;

__attribute__((swift_name("DateTimeFormatOptions")))
@protocol AssessmentModelDateTimeFormatOptions
@required
@property (readonly) BOOL allowFuture __attribute__((swift_name("allowFuture")));
@property (readonly) BOOL allowPast __attribute__((swift_name("allowPast")));
@property (readonly) NSString *codingFormat __attribute__((swift_name("codingFormat")));
@property (readonly) NSString * _Nullable maximumValue __attribute__((swift_name("maximumValue")));
@property (readonly) NSString * _Nullable minimumValue __attribute__((swift_name("minimumValue")));
@end;

__attribute__((swift_name("KeyboardTextInputItem")))
@protocol AssessmentModelKeyboardTextInputItem <AssessmentModelInputItem>
@required
- (id<AssessmentModelTextValidator>)buildTextValidator __attribute__((swift_name("buildTextValidator()")));
@property (readonly) id<AssessmentModelKeyboardOptions> keyboardOptions __attribute__((swift_name("keyboardOptions")));
@end;

__attribute__((swift_name("DateTimeInputItem")))
@protocol AssessmentModelDateTimeInputItem <AssessmentModelKeyboardTextInputItem>
@required
@property (readonly) id<AssessmentModelDateTimeFormatOptions> formatOptions __attribute__((swift_name("formatOptions")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateTimePart.Companion")))
@interface AssessmentModelDateTimePartCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (NSArray<AssessmentModelDateTimePart *> *)isoParts __attribute__((swift_name("isoParts()")));
- (NSArray<AssessmentModelDateTimePart *> *)partsForFormatString:(NSString *)formatString __attribute__((swift_name("partsFor(formatString:)")));
- (NSArray<AssessmentModelDateTimePart *> *)timestampParts __attribute__((swift_name("timestampParts()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateTimeReference")))
@interface AssessmentModelDateTimeReference : AssessmentModelBase
- (instancetype)initWithDateTimeString:(NSString *)dateTimeString timezoneIdentifier:(NSString * _Nullable)timezoneIdentifier __attribute__((swift_name("init(dateTimeString:timezoneIdentifier:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelDateTimeReference *)doCopyDateTimeString:(NSString *)dateTimeString timezoneIdentifier:(NSString * _Nullable)timezoneIdentifier __attribute__((swift_name("doCopy(dateTimeString:timezoneIdentifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *dateTimeString __attribute__((swift_name("dateTimeString")));
@property (readonly) NSString * _Nullable timezoneIdentifier __attribute__((swift_name("timezoneIdentifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateTimeReference.Companion")))
@interface AssessmentModelDateTimeReferenceCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("NodeState")))
@protocol AssessmentModelNodeState
@required
- (void)goBackwardRequestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("goBackward(requestedPermissions:asyncActionNavigations:)")));
- (void)goForwardRequestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("goForward(requestedPermissions:asyncActionNavigations:)")));
@property (readonly) id<AssessmentModelResult> currentResult __attribute__((swift_name("currentResult")));
@property (readonly) id<AssessmentModelNode> node __attribute__((swift_name("node")));
@property (readonly) id<AssessmentModelBranchNodeState> _Nullable parent __attribute__((swift_name("parent")));
@end;

__attribute__((swift_name("LeafNodeState")))
@protocol AssessmentModelLeafNodeState <AssessmentModelNodeState>
@required
@end;

__attribute__((swift_name("FormStepState")))
@protocol AssessmentModelFormStepState <AssessmentModelLeafNodeState>
@required
@property (readonly) NSArray<id<AssessmentModelFieldState>> *fieldStates __attribute__((swift_name("fieldStates")));
@end;

__attribute__((swift_name("FormStepStateImpl")))
@interface AssessmentModelFormStepStateImpl : AssessmentModelBase <AssessmentModelFormStepState>
- (instancetype)initWithNode:(id<AssessmentModelFormStep>)node parent:(id<AssessmentModelBranchNodeState>)parent __attribute__((swift_name("init(node:parent:)"))) __attribute__((objc_designated_initializer));
- (id<AssessmentModelFieldState>)fieldStateForIndex:(int32_t)index node:(id<AssessmentModelNode>)node __attribute__((swift_name("fieldStateFor(index:node:)")));
- (id<AssessmentModelResult> _Nullable)previousResultForNode:(id<AssessmentModelNode>)node __attribute__((swift_name("previousResultFor(node:)")));
- (id<AssessmentModelResult>)resultForNode:(id<AssessmentModelNode>)node __attribute__((swift_name("resultFor(node:)")));
@property (readonly) id<AssessmentModelCollectionResult> currentResult __attribute__((swift_name("currentResult")));
@property (readonly) NSArray<id<AssessmentModelFieldState>> *fieldStates __attribute__((swift_name("fieldStates")));
@property (readonly) id<AssessmentModelFormStep> node __attribute__((swift_name("node")));
@property (readonly) id<AssessmentModelBranchNodeState> parent __attribute__((swift_name("parent")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FormattedValue")))
@interface AssessmentModelFormattedValue<T> : AssessmentModelBase
- (instancetype)initWithResult:(T _Nullable)result invalidMessage:(id<AssessmentModelInvalidMessage> _Nullable)invalidMessage __attribute__((swift_name("init(result:invalidMessage:)"))) __attribute__((objc_designated_initializer));
- (T _Nullable)component1 __attribute__((swift_name("component1()")));
- (id<AssessmentModelInvalidMessage> _Nullable)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelFormattedValue<T> *)doCopyResult:(T _Nullable)result invalidMessage:(id<AssessmentModelInvalidMessage> _Nullable)invalidMessage __attribute__((swift_name("doCopy(result:invalidMessage:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<AssessmentModelInvalidMessage> _Nullable invalidMessage __attribute__((swift_name("invalidMessage")));
@property (readonly) T _Nullable result __attribute__((swift_name("result")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("GMTOffsetPart")))
@interface AssessmentModelGMTOffsetPart : AssessmentModelDateTimePart
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)gMTOffsetPart __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ISO8601Format")))
@interface AssessmentModelISO8601Format : AssessmentModelBase
- (instancetype)initWithFormatString:(NSString *)formatString regexPattern:(NSString *)regexPattern __attribute__((swift_name("init(formatString:regexPattern:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelISO8601Format *)doCopyFormatString:(NSString *)formatString regexPattern:(NSString *)regexPattern __attribute__((swift_name("doCopy(formatString:regexPattern:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *formatString __attribute__((swift_name("formatString")));
@property (readonly) NSString *regexPattern __attribute__((swift_name("regexPattern")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ISO8601Format.Companion")))
@interface AssessmentModelISO8601FormatCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) AssessmentModelISO8601Format *DateOnly __attribute__((swift_name("DateOnly")));
@property (readonly) AssessmentModelISO8601Format *HourMinuteFormat __attribute__((swift_name("HourMinuteFormat")));
@property (readonly) AssessmentModelISO8601Format *MonthDayFormat __attribute__((swift_name("MonthDayFormat")));
@property (readonly) AssessmentModelISO8601Format *MonthYearFormat __attribute__((swift_name("MonthYearFormat")));
@property (readonly) AssessmentModelISO8601Format *TimeOnly __attribute__((swift_name("TimeOnly")));
@property (readonly) AssessmentModelISO8601Format *Timestamp __attribute__((swift_name("Timestamp")));
@property (readonly) AssessmentModelISO8601Format *TimestampAndroid __attribute__((swift_name("TimestampAndroid")));
@property (readonly) AssessmentModelISO8601Format *YearFormat __attribute__((swift_name("YearFormat")));
@property (readonly) NSMutableArray<AssessmentModelISO8601Format *> *registeredFormats __attribute__((swift_name("registeredFormats")));
@end;

__attribute__((swift_name("InvalidMessage")))
@protocol AssessmentModelInvalidMessage
@required
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("InvalidMessageObject")))
@interface AssessmentModelInvalidMessageObject : AssessmentModelBase <AssessmentModelInvalidMessage>
- (instancetype)initWithString:(NSString *)string __attribute__((swift_name("init(string:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelInvalidMessageObject *)doCopyString:(NSString *)string __attribute__((swift_name("doCopy(string:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *string __attribute__((swift_name("string")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("InvalidMessageObject.Companion")))
@interface AssessmentModelInvalidMessageObjectCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelInvalidMessageObject *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelInvalidMessageObject *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("KeyboardInputItemState")))
@protocol AssessmentModelKeyboardInputItemState <AssessmentModelAnyInputItemState>
@required
@property (readonly) id<AssessmentModelTextValidator> textValidator __attribute__((swift_name("textValidator")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KeyboardInputItemStateImpl")))
@interface AssessmentModelKeyboardInputItemStateImpl<T> : AssessmentModelBase <AssessmentModelKeyboardInputItemState>
- (instancetype)initWithIndex:(int32_t)index inputItem:(id<AssessmentModelKeyboardTextInputItem>)inputItem storedAnswer:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)storedAnswer __attribute__((swift_name("init(index:inputItem:storedAnswer:)"))) __attribute__((objc_designated_initializer));
@property (readonly) int32_t index __attribute__((swift_name("index")));
@property (readonly) id<AssessmentModelKeyboardTextInputItem> inputItem __attribute__((swift_name("inputItem")));
@property BOOL selected __attribute__((swift_name("selected")));
@property AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable storedAnswer __attribute__((swift_name("storedAnswer")));
@property (readonly) id<AssessmentModelTextValidator> textValidator __attribute__((swift_name("textValidator")));
@end;

__attribute__((swift_name("KeyboardOptions")))
@protocol AssessmentModelKeyboardOptions
@required
@property (readonly) AssessmentModelAutoCapitalizationType *autocapitalizationType __attribute__((swift_name("autocapitalizationType")));
@property (readonly) AssessmentModelAutoCorrectionType *autocorrectionType __attribute__((swift_name("autocorrectionType")));
@property (readonly) BOOL isSecureTextEntry __attribute__((swift_name("isSecureTextEntry")));
@property (readonly) AssessmentModelKeyboardType *keyboardType __attribute__((swift_name("keyboardType")));
@property (readonly) AssessmentModelSpellCheckingType *spellCheckingType __attribute__((swift_name("spellCheckingType")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KeyboardType")))
@interface AssessmentModelKeyboardType : AssessmentModelKotlinEnum<AssessmentModelKeyboardType *> <AssessmentModelStringEnum>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelKeyboardType *default_ __attribute__((swift_name("default_")));
@property (class, readonly) AssessmentModelKeyboardType *asciicapable __attribute__((swift_name("asciicapable")));
@property (class, readonly) AssessmentModelKeyboardType *numbersandpunctuation __attribute__((swift_name("numbersandpunctuation")));
@property (class, readonly) AssessmentModelKeyboardType *url __attribute__((swift_name("url")));
@property (class, readonly) AssessmentModelKeyboardType *numberpad __attribute__((swift_name("numberpad")));
@property (class, readonly) AssessmentModelKeyboardType *phonepad __attribute__((swift_name("phonepad")));
@property (class, readonly) AssessmentModelKeyboardType *namephonepad __attribute__((swift_name("namephonepad")));
@property (class, readonly) AssessmentModelKeyboardType *emailaddress __attribute__((swift_name("emailaddress")));
@property (class, readonly) AssessmentModelKeyboardType *decimalpad __attribute__((swift_name("decimalpad")));
@property (class, readonly) AssessmentModelKeyboardType *twitter __attribute__((swift_name("twitter")));
@property (class, readonly) AssessmentModelKeyboardType *websearch __attribute__((swift_name("websearch")));
@property (class, readonly) AssessmentModelKeyboardType *asciicapablenumberpad __attribute__((swift_name("asciicapablenumberpad")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KeyboardType.Companion")))
@interface AssessmentModelKeyboardTypeCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelKeyboardType *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelKeyboardType *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("SkipCheckboxQuestion")))
@protocol AssessmentModelSkipCheckboxQuestion <AssessmentModelQuestion>
@required
@property (readonly) id<AssessmentModelSkipCheckboxInputItem> _Nullable skipCheckbox __attribute__((swift_name("skipCheckbox")));
@end;

__attribute__((swift_name("MultipleInputQuestion")))
@protocol AssessmentModelMultipleInputQuestion <AssessmentModelSkipCheckboxQuestion>
@required
@property (readonly) NSArray<id<AssessmentModelInputItem>> *inputItems __attribute__((swift_name("inputItems")));
@property (readonly) NSString * _Nullable sequenceSeparator __attribute__((swift_name("sequenceSeparator")));
@end;

__attribute__((swift_name("Range")))
@protocol AssessmentModelRange
@required
- (AssessmentModelFormattedValue<id> *)validateNumber:(id _Nullable)number __attribute__((swift_name("validate(number:)")));
@property (readonly) id<AssessmentModelInvalidMessage> invalidMessage __attribute__((swift_name("invalidMessage")));
@property (readonly) id<AssessmentModelInvalidMessage> _Nullable maxInvalidMessage __attribute__((swift_name("maxInvalidMessage")));
@property (readonly) id _Nullable maximumValue __attribute__((swift_name("maximumValue")));
@property (readonly) id<AssessmentModelInvalidMessage> _Nullable minInvalidMessage __attribute__((swift_name("minInvalidMessage")));
@property (readonly) id _Nullable minimumValue __attribute__((swift_name("minimumValue")));
@end;

__attribute__((swift_name("NumberRange")))
@protocol AssessmentModelNumberRange <AssessmentModelRange>
@required
@property (readonly) AssessmentModelNumberType *numberType __attribute__((swift_name("numberType")));
@property (readonly) id _Nullable stepInterval __attribute__((swift_name("stepInterval")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NumberType")))
@interface AssessmentModelNumberType : AssessmentModelKotlinEnum<AssessmentModelNumberType *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelNumberType *int_ __attribute__((swift_name("int_")));
@property (class, readonly) AssessmentModelNumberType *double_ __attribute__((swift_name("double_")));
@property (class, readonly) AssessmentModelNumberType *short_ __attribute__((swift_name("short_")));
@property (class, readonly) AssessmentModelNumberType *long_ __attribute__((swift_name("long_")));
@property (class, readonly) AssessmentModelNumberType *float_ __attribute__((swift_name("float_")));
@end;

__attribute__((swift_name("TextValidator")))
@protocol AssessmentModelTextValidator
@required
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValueForValue:(id _Nullable)value __attribute__((swift_name("jsonValueFor(value:)")));
- (AssessmentModelFormattedValue<NSString *> *)localizedStringForValue:(id _Nullable)value __attribute__((swift_name("localizedStringFor(value:)")));
- (AssessmentModelFormattedValue<id> * _Nullable)valueForText:(NSString *)text __attribute__((swift_name("valueFor(text:)")));
- (id _Nullable)valueForJsonValue:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValue __attribute__((swift_name("valueFor(jsonValue:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PassThruTextValidator")))
@interface AssessmentModelPassThruTextValidator : AssessmentModelBase <AssessmentModelTextValidator>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)passThruTextValidator __attribute__((swift_name("init()")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValueForValue:(NSString * _Nullable)value __attribute__((swift_name("jsonValueFor(value:)")));
- (AssessmentModelFormattedValue<NSString *> *)localizedStringForValue:(NSString * _Nullable)value __attribute__((swift_name("localizedStringFor(value:)")));
- (AssessmentModelFormattedValue<NSString *> * _Nullable)valueForText:(NSString *)text __attribute__((swift_name("valueFor(text:)")));
- (NSString * _Nullable)valueForJsonValue:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValue __attribute__((swift_name("valueFor(jsonValue:)")));
@end;

__attribute__((swift_name("QuestionFieldStateImpl")))
@interface AssessmentModelQuestionFieldStateImpl : AssessmentModelAbstractQuestionFieldStateImpl
- (instancetype)initWithIndex:(int32_t)index node:(id<AssessmentModelQuestion>)node currentResult:(id<AssessmentModelAnswerResult>)currentResult __attribute__((swift_name("init(index:node:currentResult:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
@property (readonly) id<AssessmentModelAnswerResult> currentResult __attribute__((swift_name("currentResult")));
@property (readonly) int32_t index __attribute__((swift_name("index")));
@property (readonly) id<AssessmentModelQuestion> node __attribute__((swift_name("node")));
@end;

__attribute__((swift_name("QuestionState")))
@protocol AssessmentModelQuestionState <AssessmentModelLeafNodeState, AssessmentModelQuestionFieldState>
@required
@end;

__attribute__((swift_name("QuestionStateImpl")))
@interface AssessmentModelQuestionStateImpl : AssessmentModelAbstractQuestionFieldStateImpl <AssessmentModelQuestionState>
- (instancetype)initWithNode:(id<AssessmentModelQuestion>)node parent:(id<AssessmentModelBranchNodeState>)parent __attribute__((swift_name("init(node:parent:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
@property (readonly) id<AssessmentModelAnswerResult> currentResult __attribute__((swift_name("currentResult")));
@property (readonly) int32_t index __attribute__((swift_name("index")));
@property (readonly) id<AssessmentModelQuestion> node __attribute__((swift_name("node")));
@property (readonly) id<AssessmentModelBranchNodeState> parent __attribute__((swift_name("parent")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ReservedNavigationIdentifier")))
@interface AssessmentModelReservedNavigationIdentifier : AssessmentModelKotlinEnum<AssessmentModelReservedNavigationIdentifier *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelReservedNavigationIdentifier *exit __attribute__((swift_name("exit")));
@property (class, readonly) AssessmentModelReservedNavigationIdentifier *nextsection __attribute__((swift_name("nextsection")));
- (BOOL)matchingIdentifier:(NSString * _Nullable)identifier __attribute__((swift_name("matching(identifier:)")));
@end;

__attribute__((swift_name("SimpleQuestion")))
@protocol AssessmentModelSimpleQuestion <AssessmentModelSkipCheckboxQuestion>
@required
@property (readonly) id<AssessmentModelInputItem> inputItem __attribute__((swift_name("inputItem")));
@end;

__attribute__((swift_name("SkipCheckboxInputItem")))
@protocol AssessmentModelSkipCheckboxInputItem <AssessmentModelChoiceInputItem>
@required
@property (readonly) AssessmentModelKotlinx_serialization_jsonJsonElement *value __attribute__((swift_name("value")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SpellCheckingType")))
@interface AssessmentModelSpellCheckingType : AssessmentModelKotlinEnum<AssessmentModelSpellCheckingType *> <AssessmentModelStringEnum>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelSpellCheckingType *default_ __attribute__((swift_name("default_")));
@property (class, readonly) AssessmentModelSpellCheckingType *no __attribute__((swift_name("no")));
@property (class, readonly) AssessmentModelSpellCheckingType *yes __attribute__((swift_name("yes")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SpellCheckingType.Companion")))
@interface AssessmentModelSpellCheckingTypeCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelSpellCheckingType *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelSpellCheckingType *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SurveyRuleOperator")))
@interface AssessmentModelSurveyRuleOperator : AssessmentModelKotlinEnum<AssessmentModelSurveyRuleOperator *> <AssessmentModelStringEnum>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelSurveyRuleOperator *equal __attribute__((swift_name("equal")));
@property (class, readonly) AssessmentModelSurveyRuleOperator *notequal __attribute__((swift_name("notequal")));
@property (class, readonly) AssessmentModelSurveyRuleOperator *lessthan __attribute__((swift_name("lessthan")));
@property (class, readonly) AssessmentModelSurveyRuleOperator *greaterthan __attribute__((swift_name("greaterthan")));
@property (class, readonly) AssessmentModelSurveyRuleOperator *lessthanequal __attribute__((swift_name("lessthanequal")));
@property (class, readonly) AssessmentModelSurveyRuleOperator *greaterthanequal __attribute__((swift_name("greaterthanequal")));
@property (class, readonly) AssessmentModelSurveyRuleOperator *always __attribute__((swift_name("always")));
@property (class, readonly) AssessmentModelSurveyRuleOperator *skip __attribute__((swift_name("skip")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SurveyRuleOperator.Companion")))
@interface AssessmentModelSurveyRuleOperatorCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("TimePart")))
@interface AssessmentModelTimePart : AssessmentModelDateTimePart
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimePart.Companion")))
@interface AssessmentModelTimePartCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (NSArray<AssessmentModelTimePart *> *)values __attribute__((swift_name("values()")));
@property (readonly) NSString *separator __attribute__((swift_name("separator")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimePart.Hour")))
@interface AssessmentModelTimePartHour : AssessmentModelTimePart
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)hour __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimePart.Minute")))
@interface AssessmentModelTimePartMinute : AssessmentModelTimePart
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)minute __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimePart.Second")))
@interface AssessmentModelTimePartSecond : AssessmentModelTimePart
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)second __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimeZoneIdentifierPart")))
@interface AssessmentModelTimeZoneIdentifierPart : AssessmentModelDateTimePart
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)timeZoneIdentifierPart __attribute__((swift_name("init()")));
@end;

__attribute__((swift_name("UIHint")))
@interface AssessmentModelUIHint : AssessmentModelBase <AssessmentModelStringEnum>
@end;

__attribute__((swift_name("UIHint.Choice")))
@interface AssessmentModelUIHintChoice : AssessmentModelUIHint
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.ChoiceCheckbox")))
@interface AssessmentModelUIHintChoiceCheckbox : AssessmentModelUIHintChoice
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)checkbox __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.ChoiceCheckmark")))
@interface AssessmentModelUIHintChoiceCheckmark : AssessmentModelUIHintChoice
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)checkmark __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.ChoiceCompanion")))
@interface AssessmentModelUIHintChoiceCompanion : AssessmentModelBase <AssessmentModelStringEnumCompanion, AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelUIHintChoice *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelUIHintChoice *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (AssessmentModelKotlinArray<AssessmentModelUIHintChoice *> *)values __attribute__((swift_name("values()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.ChoiceListItem")))
@interface AssessmentModelUIHintChoiceListItem : AssessmentModelUIHintChoice
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)listItem __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.ChoiceRadioButton")))
@interface AssessmentModelUIHintChoiceRadioButton : AssessmentModelUIHintChoice
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)radioButton __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.Companion")))
@interface AssessmentModelUIHintCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelUIHint *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelUIHint *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (AssessmentModelUIHint *)valueOfName:(NSString *)name __attribute__((swift_name("valueOf(name:)")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.Custom")))
@interface AssessmentModelUIHintCustom : AssessmentModelUIHint
- (instancetype)initWithName:(NSString *)name __attribute__((swift_name("init(name:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelUIHintCustom *)doCopyName:(NSString *)name __attribute__((swift_name("doCopy(name:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((swift_name("UIHint.Detail")))
@interface AssessmentModelUIHintDetail : AssessmentModelUIHint
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.DetailButton")))
@interface AssessmentModelUIHintDetailButton : AssessmentModelUIHintDetail
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)button __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.DetailCompanion")))
@interface AssessmentModelUIHintDetailCompanion : AssessmentModelBase <AssessmentModelStringEnumCompanion>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelKotlinArray<AssessmentModelUIHintDetail *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.DetailDisclosureArrow")))
@interface AssessmentModelUIHintDetailDisclosureArrow : AssessmentModelUIHintDetail
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)disclosureArrow __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.DetailLink")))
@interface AssessmentModelUIHintDetailLink : AssessmentModelUIHintDetail
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)link __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.Picker")))
@interface AssessmentModelUIHintPicker : AssessmentModelUIHint
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)picker __attribute__((swift_name("init()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((swift_name("UIHint.TextField")))
@interface AssessmentModelUIHintTextField : AssessmentModelUIHint
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.TextFieldCompanion")))
@interface AssessmentModelUIHintTextFieldCompanion : AssessmentModelBase <AssessmentModelStringEnumCompanion, AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelUIHintTextField *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelUIHintTextField *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (AssessmentModelKotlinArray<AssessmentModelUIHintTextField *> *)values __attribute__((swift_name("values()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.TextFieldDefault")))
@interface AssessmentModelUIHintTextFieldDefault : AssessmentModelUIHintTextField
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)default_ __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.TextFieldMultipleLine")))
@interface AssessmentModelUIHintTextFieldMultipleLine : AssessmentModelUIHintTextField
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)multipleLine __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UIHint.TextFieldPopover")))
@interface AssessmentModelUIHintTextFieldPopover : AssessmentModelUIHintTextField
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)popover __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AttitudeReferenceFrame")))
@interface AssessmentModelAttitudeReferenceFrame : AssessmentModelKotlinEnum<AssessmentModelAttitudeReferenceFrame *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelAttitudeReferenceFrame *xarbitraryzvertical __attribute__((swift_name("xarbitraryzvertical")));
@property (class, readonly) AssessmentModelAttitudeReferenceFrame *xmagneticnorthzvertical __attribute__((swift_name("xmagneticnorthzvertical")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AttitudeReferenceFrame.Companion")))
@interface AssessmentModelAttitudeReferenceFrameCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DistanceRecord")))
@interface AssessmentModelDistanceRecord : AssessmentModelBase <AssessmentModelSampleRecord>
- (instancetype)initWithStepPath:(NSString * _Nullable)stepPath timestampDateString:(NSString * _Nullable)timestampDateString timestamp:(AssessmentModelDouble * _Nullable)timestamp uptime:(AssessmentModelDouble * _Nullable)uptime timestampUnix:(AssessmentModelDouble * _Nullable)timestampUnix horizontalAccuracy:(AssessmentModelDouble * _Nullable)horizontalAccuracy relativeDistance:(AssessmentModelDouble * _Nullable)relativeDistance latitude:(AssessmentModelDouble * _Nullable)latitude longitude:(AssessmentModelDouble * _Nullable)longitude verticalAccuracy:(AssessmentModelDouble * _Nullable)verticalAccuracy altitude:(AssessmentModelDouble * _Nullable)altitude totalDistance:(AssessmentModelDouble * _Nullable)totalDistance course:(AssessmentModelDouble * _Nullable)course bearingRadians:(AssessmentModelDouble * _Nullable)bearingRadians speed:(AssessmentModelDouble * _Nullable)speed floor:(AssessmentModelInt * _Nullable)floor __attribute__((swift_name("init(stepPath:timestampDateString:timestamp:uptime:timestampUnix:horizontalAccuracy:relativeDistance:latitude:longitude:verticalAccuracy:altitude:totalDistance:course:bearingRadians:speed:floor:)"))) __attribute__((objc_designated_initializer));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelDouble * _Nullable)component10 __attribute__((swift_name("component10()")));
- (AssessmentModelDouble * _Nullable)component11 __attribute__((swift_name("component11()")));
- (AssessmentModelDouble * _Nullable)component12 __attribute__((swift_name("component12()")));
- (AssessmentModelDouble * _Nullable)component13 __attribute__((swift_name("component13()")));
- (AssessmentModelDouble * _Nullable)component14 __attribute__((swift_name("component14()")));
- (AssessmentModelDouble * _Nullable)component15 __attribute__((swift_name("component15()")));
- (AssessmentModelInt * _Nullable)component16 __attribute__((swift_name("component16()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelDouble * _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelDouble * _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelDouble * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelDouble * _Nullable)component6 __attribute__((swift_name("component6()")));
- (AssessmentModelDouble * _Nullable)component7 __attribute__((swift_name("component7()")));
- (AssessmentModelDouble * _Nullable)component8 __attribute__((swift_name("component8()")));
- (AssessmentModelDouble * _Nullable)component9 __attribute__((swift_name("component9()")));
- (AssessmentModelDistanceRecord *)doCopyStepPath:(NSString * _Nullable)stepPath timestampDateString:(NSString * _Nullable)timestampDateString timestamp:(AssessmentModelDouble * _Nullable)timestamp uptime:(AssessmentModelDouble * _Nullable)uptime timestampUnix:(AssessmentModelDouble * _Nullable)timestampUnix horizontalAccuracy:(AssessmentModelDouble * _Nullable)horizontalAccuracy relativeDistance:(AssessmentModelDouble * _Nullable)relativeDistance latitude:(AssessmentModelDouble * _Nullable)latitude longitude:(AssessmentModelDouble * _Nullable)longitude verticalAccuracy:(AssessmentModelDouble * _Nullable)verticalAccuracy altitude:(AssessmentModelDouble * _Nullable)altitude totalDistance:(AssessmentModelDouble * _Nullable)totalDistance course:(AssessmentModelDouble * _Nullable)course bearingRadians:(AssessmentModelDouble * _Nullable)bearingRadians speed:(AssessmentModelDouble * _Nullable)speed floor:(AssessmentModelInt * _Nullable)floor __attribute__((swift_name("doCopy(stepPath:timestampDateString:timestamp:uptime:timestampUnix:horizontalAccuracy:relativeDistance:latitude:longitude:verticalAccuracy:altitude:totalDistance:course:bearingRadians:speed:floor:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelDouble * _Nullable altitude __attribute__((swift_name("altitude")));
@property (readonly) AssessmentModelDouble * _Nullable bearingRadians __attribute__((swift_name("bearingRadians")));
@property (readonly) AssessmentModelDouble * _Nullable course __attribute__((swift_name("course")));
@property (readonly) AssessmentModelInt * _Nullable floor __attribute__((swift_name("floor")));
@property (readonly) AssessmentModelDouble * _Nullable horizontalAccuracy __attribute__((swift_name("horizontalAccuracy")));
@property (readonly) AssessmentModelDouble * _Nullable latitude __attribute__((swift_name("latitude")));
@property (readonly) AssessmentModelDouble * _Nullable longitude __attribute__((swift_name("longitude")));
@property (readonly) AssessmentModelDouble * _Nullable relativeDistance __attribute__((swift_name("relativeDistance")));
@property (readonly) AssessmentModelDouble * _Nullable speed __attribute__((swift_name("speed")));
@property (readonly) NSString * _Nullable stepPath __attribute__((swift_name("stepPath")));
@property (readonly) AssessmentModelDouble * _Nullable timestamp __attribute__((swift_name("timestamp")));
@property (readonly) NSString * _Nullable timestampDateString __attribute__((swift_name("timestampDateString")));
@property (readonly) AssessmentModelDouble * _Nullable timestampUnix __attribute__((swift_name("timestampUnix")));
@property (readonly) AssessmentModelDouble * _Nullable totalDistance __attribute__((swift_name("totalDistance")));
@property (readonly) AssessmentModelDouble * _Nullable uptime __attribute__((swift_name("uptime")));
@property (readonly) AssessmentModelDouble * _Nullable verticalAccuracy __attribute__((swift_name("verticalAccuracy")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DistanceRecord.Companion")))
@interface AssessmentModelDistanceRecordCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DistanceRecorderConfiguration")))
@interface AssessmentModelDistanceRecorderConfiguration : AssessmentModelBase <AssessmentModelTableRecorderConfiguration>
- (instancetype)initWithIdentifier:(NSString *)identifier comment:(NSString * _Nullable)comment reason:(NSString * _Nullable)reason optional:(BOOL)optional motionStepIdentifier:(NSString * _Nullable)motionStepIdentifier startStepIdentifier:(NSString * _Nullable)startStepIdentifier stopStepIdentifier:(NSString * _Nullable)stopStepIdentifier shouldDeletePrevious:(BOOL)shouldDeletePrevious usesCSVEncoding:(BOOL)usesCSVEncoding __attribute__((swift_name("init(identifier:comment:reason:optional:motionStepIdentifier:startStepIdentifier:stopStepIdentifier:shouldDeletePrevious:usesCSVEncoding:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (BOOL)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (NSString * _Nullable)component6 __attribute__((swift_name("component6()")));
- (NSString * _Nullable)component7 __attribute__((swift_name("component7()")));
- (BOOL)component8 __attribute__((swift_name("component8()")));
- (BOOL)component9 __attribute__((swift_name("component9()")));
- (AssessmentModelDistanceRecorderConfiguration *)doCopyIdentifier:(NSString *)identifier comment:(NSString * _Nullable)comment reason:(NSString * _Nullable)reason optional:(BOOL)optional motionStepIdentifier:(NSString * _Nullable)motionStepIdentifier startStepIdentifier:(NSString * _Nullable)startStepIdentifier stopStepIdentifier:(NSString * _Nullable)stopStepIdentifier shouldDeletePrevious:(BOOL)shouldDeletePrevious usesCSVEncoding:(BOOL)usesCSVEncoding __attribute__((swift_name("doCopy(identifier:comment:reason:optional:motionStepIdentifier:startStepIdentifier:stopStepIdentifier:shouldDeletePrevious:usesCSVEncoding:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable comment __attribute__((swift_name("comment")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) NSString * _Nullable motionStepIdentifier __attribute__((swift_name("motionStepIdentifier")));
@property (readonly) BOOL optional __attribute__((swift_name("optional")));
@property (readonly) NSArray<id<AssessmentModelPermissionInfo>> * _Nullable permissions __attribute__((swift_name("permissions")));
@property (readonly) NSString * _Nullable reason __attribute__((swift_name("reason")));
@property (readonly) BOOL requiresBackground __attribute__((swift_name("requiresBackground")));
@property (readonly) BOOL shouldDeletePrevious __attribute__((swift_name("shouldDeletePrevious")));
@property (readonly) NSString * _Nullable startStepIdentifier __attribute__((swift_name("startStepIdentifier")));
@property (readonly) NSString * _Nullable stopStepIdentifier __attribute__((swift_name("stopStepIdentifier")));
@property (readonly) BOOL usesCSVEncoding __attribute__((swift_name("usesCSVEncoding")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DistanceRecorderConfiguration.Companion")))
@interface AssessmentModelDistanceRecorderConfigurationCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MotionRecord")))
@interface AssessmentModelMotionRecord : AssessmentModelBase <AssessmentModelSampleRecord>
- (instancetype)initWithStepPath:(NSString * _Nullable)stepPath timestampDateString:(NSString * _Nullable)timestampDateString timestamp:(AssessmentModelDouble * _Nullable)timestamp uptime:(AssessmentModelDouble * _Nullable)uptime sensorType:(AssessmentModelMotionRecorderType * _Nullable)sensorType x:(AssessmentModelDouble * _Nullable)x y:(AssessmentModelDouble * _Nullable)y z:(AssessmentModelDouble * _Nullable)z w:(AssessmentModelDouble * _Nullable)w eventAccuracy:(AssessmentModelInt * _Nullable)eventAccuracy referenceCoordinate:(AssessmentModelAttitudeReferenceFrame * _Nullable)referenceCoordinate heading:(AssessmentModelDouble * _Nullable)heading __attribute__((swift_name("init(stepPath:timestampDateString:timestamp:uptime:sensorType:x:y:z:w:eventAccuracy:referenceCoordinate:heading:)"))) __attribute__((objc_designated_initializer));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelInt * _Nullable)component10 __attribute__((swift_name("component10()")));
- (AssessmentModelAttitudeReferenceFrame * _Nullable)component11 __attribute__((swift_name("component11()")));
- (AssessmentModelDouble * _Nullable)component12 __attribute__((swift_name("component12()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelDouble * _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelDouble * _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelMotionRecorderType * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelDouble * _Nullable)component6 __attribute__((swift_name("component6()")));
- (AssessmentModelDouble * _Nullable)component7 __attribute__((swift_name("component7()")));
- (AssessmentModelDouble * _Nullable)component8 __attribute__((swift_name("component8()")));
- (AssessmentModelDouble * _Nullable)component9 __attribute__((swift_name("component9()")));
- (AssessmentModelMotionRecord *)doCopyStepPath:(NSString * _Nullable)stepPath timestampDateString:(NSString * _Nullable)timestampDateString timestamp:(AssessmentModelDouble * _Nullable)timestamp uptime:(AssessmentModelDouble * _Nullable)uptime sensorType:(AssessmentModelMotionRecorderType * _Nullable)sensorType x:(AssessmentModelDouble * _Nullable)x y:(AssessmentModelDouble * _Nullable)y z:(AssessmentModelDouble * _Nullable)z w:(AssessmentModelDouble * _Nullable)w eventAccuracy:(AssessmentModelInt * _Nullable)eventAccuracy referenceCoordinate:(AssessmentModelAttitudeReferenceFrame * _Nullable)referenceCoordinate heading:(AssessmentModelDouble * _Nullable)heading __attribute__((swift_name("doCopy(stepPath:timestampDateString:timestamp:uptime:sensorType:x:y:z:w:eventAccuracy:referenceCoordinate:heading:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelInt * _Nullable eventAccuracy __attribute__((swift_name("eventAccuracy")));
@property (readonly) AssessmentModelDouble * _Nullable heading __attribute__((swift_name("heading")));
@property (readonly) AssessmentModelAttitudeReferenceFrame * _Nullable referenceCoordinate __attribute__((swift_name("referenceCoordinate")));
@property (readonly) AssessmentModelMotionRecorderType * _Nullable sensorType __attribute__((swift_name("sensorType")));
@property (readonly) NSString * _Nullable stepPath __attribute__((swift_name("stepPath")));
@property (readonly) AssessmentModelDouble * _Nullable timestamp __attribute__((swift_name("timestamp")));
@property (readonly) NSString * _Nullable timestampDateString __attribute__((swift_name("timestampDateString")));
@property (readonly) AssessmentModelDouble * _Nullable uptime __attribute__((swift_name("uptime")));
@property (readonly) AssessmentModelDouble * _Nullable w __attribute__((swift_name("w")));
@property (readonly) AssessmentModelDouble * _Nullable x __attribute__((swift_name("x")));
@property (readonly) AssessmentModelDouble * _Nullable y __attribute__((swift_name("y")));
@property (readonly) AssessmentModelDouble * _Nullable z __attribute__((swift_name("z")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MotionRecord.Companion")))
@interface AssessmentModelMotionRecordCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MotionRecorderConfiguration")))
@interface AssessmentModelMotionRecorderConfiguration : AssessmentModelBase <AssessmentModelTableRecorderConfiguration>
- (instancetype)initWithIdentifier:(NSString *)identifier comment:(NSString * _Nullable)comment reason:(NSString * _Nullable)reason optional:(BOOL)optional startStepIdentifier:(NSString * _Nullable)startStepIdentifier stopStepIdentifier:(NSString * _Nullable)stopStepIdentifier requiresBackground:(BOOL)requiresBackground shouldDeletePrevious:(BOOL)shouldDeletePrevious usesCSVEncoding:(BOOL)usesCSVEncoding recorderTypes:(NSSet<AssessmentModelMotionRecorderType *> *)recorderTypes samplingFrequency:(AssessmentModelDouble * _Nullable)samplingFrequency __attribute__((swift_name("init(identifier:comment:reason:optional:startStepIdentifier:stopStepIdentifier:requiresBackground:shouldDeletePrevious:usesCSVEncoding:recorderTypes:samplingFrequency:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSSet<AssessmentModelMotionRecorderType *> *)component10 __attribute__((swift_name("component10()")));
- (AssessmentModelDouble * _Nullable)component11 __attribute__((swift_name("component11()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (BOOL)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (NSString * _Nullable)component6 __attribute__((swift_name("component6()")));
- (BOOL)component7 __attribute__((swift_name("component7()")));
- (BOOL)component8 __attribute__((swift_name("component8()")));
- (BOOL)component9 __attribute__((swift_name("component9()")));
- (AssessmentModelMotionRecorderConfiguration *)doCopyIdentifier:(NSString *)identifier comment:(NSString * _Nullable)comment reason:(NSString * _Nullable)reason optional:(BOOL)optional startStepIdentifier:(NSString * _Nullable)startStepIdentifier stopStepIdentifier:(NSString * _Nullable)stopStepIdentifier requiresBackground:(BOOL)requiresBackground shouldDeletePrevious:(BOOL)shouldDeletePrevious usesCSVEncoding:(BOOL)usesCSVEncoding recorderTypes:(NSSet<AssessmentModelMotionRecorderType *> *)recorderTypes samplingFrequency:(AssessmentModelDouble * _Nullable)samplingFrequency __attribute__((swift_name("doCopy(identifier:comment:reason:optional:startStepIdentifier:stopStepIdentifier:requiresBackground:shouldDeletePrevious:usesCSVEncoding:recorderTypes:samplingFrequency:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable comment __attribute__((swift_name("comment")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) BOOL optional __attribute__((swift_name("optional")));
@property (readonly) NSArray<id<AssessmentModelPermissionInfo>> * _Nullable permissions __attribute__((swift_name("permissions")));
@property (readonly) NSString * _Nullable reason __attribute__((swift_name("reason")));
@property (readonly) NSSet<AssessmentModelMotionRecorderType *> *recorderTypes __attribute__((swift_name("recorderTypes")));
@property (readonly) BOOL requiresBackground __attribute__((swift_name("requiresBackground")));
@property (readonly) AssessmentModelDouble * _Nullable samplingFrequency __attribute__((swift_name("samplingFrequency")));
@property (readonly) BOOL shouldDeletePrevious __attribute__((swift_name("shouldDeletePrevious")));
@property (readonly) NSString * _Nullable startStepIdentifier __attribute__((swift_name("startStepIdentifier")));
@property (readonly) NSString * _Nullable stopStepIdentifier __attribute__((swift_name("stopStepIdentifier")));
@property (readonly) BOOL usesCSVEncoding __attribute__((swift_name("usesCSVEncoding")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MotionRecorderConfiguration.Companion")))
@interface AssessmentModelMotionRecorderConfigurationCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MotionRecorderType")))
@interface AssessmentModelMotionRecorderType : AssessmentModelKotlinEnum<AssessmentModelMotionRecorderType *> <AssessmentModelStringEnum>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelMotionRecorderType *accelerometer __attribute__((swift_name("accelerometer")));
@property (class, readonly) AssessmentModelMotionRecorderType *gyro __attribute__((swift_name("gyro")));
@property (class, readonly) AssessmentModelMotionRecorderType *magnetometer __attribute__((swift_name("magnetometer")));
@property (class, readonly) AssessmentModelMotionRecorderType *attitude __attribute__((swift_name("attitude")));
@property (class, readonly) AssessmentModelMotionRecorderType *gravity __attribute__((swift_name("gravity")));
@property (class, readonly) AssessmentModelMotionRecorderType *magneticfield __attribute__((swift_name("magneticfield")));
@property (class, readonly) AssessmentModelMotionRecorderType *rotationrate __attribute__((swift_name("rotationrate")));
@property (class, readonly) AssessmentModelMotionRecorderType *useracceleration __attribute__((swift_name("useracceleration")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MotionRecorderType.Companion")))
@interface AssessmentModelMotionRecorderTypeCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelMotionRecorderType *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelMotionRecorderType *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) NSSet<AssessmentModelMotionRecorderType *> *calculated __attribute__((swift_name("calculated")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@property (readonly) NSSet<AssessmentModelMotionRecorderType *> *raw __attribute__((swift_name("raw")));
@end;

__attribute__((swift_name("FileLoader")))
@protocol AssessmentModelFileLoader
@required
- (NSString *)loadFileAssetInfo:(id<AssessmentModelAssetInfo>)assetInfo resourceInfo:(id<AssessmentModelResourceInfo>)resourceInfo __attribute__((swift_name("loadFile(assetInfo:resourceInfo:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("StandardResourceAssetType")))
@interface AssessmentModelStandardResourceAssetType : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)standardResourceAssetType __attribute__((swift_name("init()")));
@property (readonly) NSString *COLOR __attribute__((swift_name("COLOR")));
@property (readonly) NSString *DRAWABLE __attribute__((swift_name("DRAWABLE")));
@property (readonly) NSString *FONT __attribute__((swift_name("FONT")));
@property (readonly) NSString *RAW __attribute__((swift_name("RAW")));
@end;

__attribute__((swift_name("NavigationRule")))
@protocol AssessmentModelNavigationRule
@required
- (NSString * _Nullable)nextNodeIdentifierBranchResult:(id<AssessmentModelBranchNodeResult>)branchResult isPeeking:(BOOL)isPeeking __attribute__((swift_name("nextNodeIdentifier(branchResult:isPeeking:)")));
@end;

__attribute__((swift_name("DirectNavigationRule")))
@protocol AssessmentModelDirectNavigationRule <AssessmentModelNavigationRule>
@required
@property (readonly) NSString * _Nullable nextNodeIdentifier __attribute__((swift_name("nextNodeIdentifier")));
@end;

__attribute__((swift_name("NodeObject")))
@interface AssessmentModelNodeObject : AssessmentModelBase <AssessmentModelContentNode, AssessmentModelDirectNavigationRule>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)doCopyFromOriginal:(id<AssessmentModelContentNode>)original __attribute__((swift_name("doCopyFrom(original:)")));
- (void)setButtonKey:(AssessmentModelButtonAction *)key value:(id<AssessmentModelButtonActionInfo> _Nullable)value __attribute__((swift_name("setButton(key:value:)")));
@property NSDictionary<AssessmentModelButtonAction *, id<AssessmentModelButtonActionInfo>> *buttonMap __attribute__((swift_name("buttonMap")));
@property NSString * _Nullable comment __attribute__((swift_name("comment")));
@property NSString * _Nullable detail __attribute__((swift_name("detail")));
@property NSString * _Nullable footnote __attribute__((swift_name("footnote")));
@property NSArray<AssessmentModelButtonAction *> *hideButtons __attribute__((swift_name("hideButtons")));
@property NSString * _Nullable nextNodeIdentifier __attribute__((swift_name("nextNodeIdentifier")));
@property NSString * _Nullable subtitle __attribute__((swift_name("subtitle")));
@property NSString * _Nullable title __attribute__((swift_name("title")));
@end;

__attribute__((swift_name("StepObject")))
@interface AssessmentModelStepObject : AssessmentModelNodeObject <AssessmentModelStep>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)doCopyFromOriginal:(id<AssessmentModelContentNode>)original __attribute__((swift_name("doCopyFrom(original:)")));
@property NSDictionary<AssessmentModelSpokenInstructionTiming *, NSString *> * _Nullable spokenInstructions __attribute__((swift_name("spokenInstructions")));
@property AssessmentModelViewThemeObject * _Nullable viewTheme __attribute__((swift_name("viewTheme")));
@end;

__attribute__((swift_name("BaseActiveStepObject")))
@interface AssessmentModelBaseActiveStepObject : AssessmentModelStepObject <AssessmentModelActiveStep>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)doCopyFromOriginal:(id<AssessmentModelContentNode>)original __attribute__((swift_name("doCopyFrom(original:)")));
@property NSSet<AssessmentModelActiveStepCommand *> *commands __attribute__((swift_name("commands")));
@property id<AssessmentModelImageInfo> _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@property BOOL requiresBackgroundAudio __attribute__((swift_name("requiresBackgroundAudio")));
@property BOOL shouldEndOnInterrupt __attribute__((swift_name("shouldEndOnInterrupt")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ActiveStepObject")))
@interface AssessmentModelActiveStepObject : AssessmentModelBaseActiveStepObject
- (instancetype)initWithIdentifier:(NSString *)identifier duration:(double)duration __attribute__((swift_name("init(identifier:duration:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (double)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelActiveStepObject *)doCopyIdentifier:(NSString *)identifier duration:(double)duration __attribute__((swift_name("doCopy(identifier:duration:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double duration __attribute__((swift_name("duration")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ActiveStepObject.Companion")))
@interface AssessmentModelActiveStepObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("ImageTheme")))
@protocol AssessmentModelImageTheme <AssessmentModelDrawableLayout>
@required
@property (readonly) AssessmentModelImagePlacement * _Nullable imagePlacement __attribute__((swift_name("imagePlacement")));
@property (readonly) AssessmentModelSize * _Nullable size __attribute__((swift_name("size")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnimatedImage")))
@interface AssessmentModelAnimatedImage : AssessmentModelBase <AssessmentModelAnimatedImageInfo, AssessmentModelImageTheme>
- (instancetype)initWithImageNames:(NSArray<NSString *> *)imageNames animationDuration:(double)animationDuration animationRepeatCount:(AssessmentModelInt * _Nullable)animationRepeatCount label:(NSString * _Nullable)label imagePlacement:(AssessmentModelImagePlacement * _Nullable)imagePlacement size:(AssessmentModelSize * _Nullable)size decoderBundle:(id _Nullable)decoderBundle bundleIdentifier:(NSString * _Nullable)bundleIdentifier packageName:(NSString * _Nullable)packageName rawFileExtension:(NSString * _Nullable)rawFileExtension versionString:(NSString * _Nullable)versionString __attribute__((swift_name("init(imageNames:animationDuration:animationRepeatCount:label:imagePlacement:size:decoderBundle:bundleIdentifier:packageName:rawFileExtension:versionString:)"))) __attribute__((objc_designated_initializer));
- (NSArray<NSString *> *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component10 __attribute__((swift_name("component10()")));
- (NSString * _Nullable)component11 __attribute__((swift_name("component11()")));
- (double)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelInt * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelImagePlacement * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelSize * _Nullable)component6 __attribute__((swift_name("component6()")));
- (id _Nullable)component7 __attribute__((swift_name("component7()")));
- (NSString * _Nullable)component8 __attribute__((swift_name("component8()")));
- (NSString * _Nullable)component9 __attribute__((swift_name("component9()")));
- (AssessmentModelAnimatedImage *)doCopyImageNames:(NSArray<NSString *> *)imageNames animationDuration:(double)animationDuration animationRepeatCount:(AssessmentModelInt * _Nullable)animationRepeatCount label:(NSString * _Nullable)label imagePlacement:(AssessmentModelImagePlacement * _Nullable)imagePlacement size:(AssessmentModelSize * _Nullable)size decoderBundle:(id _Nullable)decoderBundle bundleIdentifier:(NSString * _Nullable)bundleIdentifier packageName:(NSString * _Nullable)packageName rawFileExtension:(NSString * _Nullable)rawFileExtension versionString:(NSString * _Nullable)versionString __attribute__((swift_name("doCopy(imageNames:animationDuration:animationRepeatCount:label:imagePlacement:size:decoderBundle:bundleIdentifier:packageName:rawFileExtension:versionString:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double animationDuration __attribute__((swift_name("animationDuration")));
@property (readonly) AssessmentModelInt * _Nullable animationRepeatCount __attribute__((swift_name("animationRepeatCount")));
@property (readonly) NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property id _Nullable decoderBundle __attribute__((swift_name("decoderBundle")));
@property (readonly) NSString *imageName __attribute__((swift_name("imageName")));
@property (readonly) NSArray<NSString *> *imageNames __attribute__((swift_name("imageNames")));
@property (readonly) AssessmentModelImagePlacement * _Nullable imagePlacement __attribute__((swift_name("imagePlacement")));
@property (readonly) NSString * _Nullable label __attribute__((swift_name("label")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@property (readonly) NSString * _Nullable rawFileExtension __attribute__((swift_name("rawFileExtension")));
@property (readonly) AssessmentModelSize * _Nullable size __attribute__((swift_name("size")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnimatedImage.Companion")))
@interface AssessmentModelAnimatedImageCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerResultObject")))
@interface AssessmentModelAnswerResultObject : AssessmentModelBase <AssessmentModelAnswerResult>
- (instancetype)initWithIdentifier:(NSString *)identifier answerType:(AssessmentModelAnswerType * _Nullable)answerType jsonValue:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValue startDateString:(NSString *)startDateString endDateString:(NSString * _Nullable)endDateString __attribute__((swift_name("init(identifier:answerType:jsonValue:startDateString:endDateString:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelAnswerType * _Nullable)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString *)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelAnswerResultObject *)doCopyIdentifier:(NSString *)identifier answerType:(AssessmentModelAnswerType * _Nullable)answerType jsonValue:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValue startDateString:(NSString *)startDateString endDateString:(NSString * _Nullable)endDateString __attribute__((swift_name("doCopy(identifier:answerType:jsonValue:startDateString:endDateString:)")));
- (id<AssessmentModelAnswerResult>)doCopyResultIdentifier:(NSString *)identifier __attribute__((swift_name("doCopyResult(identifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property AssessmentModelAnswerType * _Nullable answerType __attribute__((swift_name("answerType")));
@property NSString * _Nullable endDateString __attribute__((swift_name("endDateString")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable jsonValue __attribute__((swift_name("jsonValue")));
@property NSString *startDateString __attribute__((swift_name("startDateString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerResultObject.Companion")))
@interface AssessmentModelAnswerResultObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("AssessmentGroupInfo")))
@protocol AssessmentModelAssessmentGroupInfo
@required
@property (readonly) NSArray<id<AssessmentModelAssessment>> *assessments __attribute__((swift_name("assessments")));
@property (readonly) id<AssessmentModelResourceInfo> resourceInfo __attribute__((swift_name("resourceInfo")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentGroupInfoObject")))
@interface AssessmentModelAssessmentGroupInfoObject : AssessmentModelBase <AssessmentModelResourceInfo, AssessmentModelAssessmentGroupInfo>
- (instancetype)initWithAssessments:(NSArray<id<AssessmentModelAssessment>> *)assessments packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("init(assessments:packageName:bundleIdentifier:)"))) __attribute__((objc_designated_initializer));
- (NSArray<id<AssessmentModelAssessment>> *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelAssessmentGroupInfoObject *)doCopyAssessments:(NSArray<id<AssessmentModelAssessment>> *)assessments packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("doCopy(assessments:packageName:bundleIdentifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<id<AssessmentModelAssessment>> *assessments __attribute__((swift_name("assessments")));
@property (readonly) NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property id _Nullable decoderBundle __attribute__((swift_name("decoderBundle")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@property (readonly) id<AssessmentModelResourceInfo> resourceInfo __attribute__((swift_name("resourceInfo")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentGroupInfoObject.Companion")))
@interface AssessmentModelAssessmentGroupInfoObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("KotlinDecodable")))
@interface AssessmentModelKotlinDecodable : AssessmentModelBase
- (instancetype)initWithDecoder:(AssessmentModelKotlinDecoder *)decoder __attribute__((swift_name("init(decoder:)"))) __attribute__((objc_designated_initializer));
@property (readonly) AssessmentModelKotlinDecoder *decoder __attribute__((swift_name("decoder")));
@property (readonly) NSString * _Nullable jsonString __attribute__((swift_name("jsonString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentGroupStringLoader")))
@interface AssessmentModelAssessmentGroupStringLoader : AssessmentModelKotlinDecodable
- (instancetype)initWithJsonString:(NSString *)jsonString bundle:(NSBundle *)bundle __attribute__((swift_name("init(jsonString:bundle:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithDecoder:(AssessmentModelKotlinDecoder *)decoder __attribute__((swift_name("init(decoder:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));

/**
 @note This method converts all Kotlin exceptions to errors.
*/
- (AssessmentModelAssessmentGroupWrapper * _Nullable)decodeObjectAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("decodeObject()")));
@property (readonly) NSString *jsonString __attribute__((swift_name("jsonString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentGroupWrapper")))
@interface AssessmentModelAssessmentGroupWrapper : AssessmentModelBase
- (instancetype)initWithAssessmentGroupInfo:(id<AssessmentModelAssessmentGroupInfo>)assessmentGroupInfo assessments:(NSArray<AssessmentModelAssessmentLoader *> *)assessments __attribute__((swift_name("init(assessmentGroupInfo:assessments:)"))) __attribute__((objc_designated_initializer));
- (id<AssessmentModelAssessmentGroupInfo>)component1 __attribute__((swift_name("component1()")));
- (NSArray<AssessmentModelAssessmentLoader *> *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelAssessmentGroupWrapper *)doCopyAssessmentGroupInfo:(id<AssessmentModelAssessmentGroupInfo>)assessmentGroupInfo assessments:(NSArray<AssessmentModelAssessmentLoader *> *)assessments __attribute__((swift_name("doCopy(assessmentGroupInfo:assessments:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) id<AssessmentModelAssessmentGroupInfo> assessmentGroupInfo __attribute__((swift_name("assessmentGroupInfo")));
@property (readonly) NSArray<AssessmentModelAssessmentLoader *> *assessments __attribute__((swift_name("assessments")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentJsonStringLoader")))
@interface AssessmentModelAssessmentJsonStringLoader : AssessmentModelKotlinDecodable
- (instancetype)initWithJsonString:(NSString *)jsonString bundle:(NSBundle *)bundle __attribute__((swift_name("init(jsonString:bundle:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithDecoder:(AssessmentModelKotlinDecoder *)decoder __attribute__((swift_name("init(decoder:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));

/**
 @note This method converts all Kotlin exceptions to errors.
*/
- (id<AssessmentModelAssessment> _Nullable)decodeObjectAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("decodeObject()")));
@property (readonly) NSString *jsonString __attribute__((swift_name("jsonString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentLoader")))
@interface AssessmentModelAssessmentLoader : AssessmentModelBase <AssessmentModelAssessment>
- (instancetype)initWithPlaceholder:(id<AssessmentModelAssessment>)placeholder decoder:(AssessmentModelKotlinDecoder *)decoder __attribute__((swift_name("init(placeholder:decoder:)"))) __attribute__((objc_designated_initializer));
- (id<AssessmentModelAssessmentResult>)createResult __attribute__((swift_name("createResult()")));

/**
 @note This method converts all Kotlin exceptions to errors.
*/
- (id<AssessmentModelAssessment> _Nullable)decodeObjectAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("decodeObject()")));
- (id<AssessmentModelNavigator>)getNavigator __attribute__((swift_name("getNavigator()")));
- (id<AssessmentModelAssessment>)unpackFileLoader:(id<AssessmentModelFileLoader>)fileLoader resourceInfo:(id<AssessmentModelResourceInfo>)resourceInfo jsonCoder:(AssessmentModelKotlinx_serialization_jsonJson *)jsonCoder __attribute__((swift_name("unpack(fileLoader:resourceInfo:jsonCoder:)")));
@property (readonly) NSDictionary<AssessmentModelButtonAction *, id<AssessmentModelButtonActionInfo>> *buttonMap __attribute__((swift_name("buttonMap")));
@property (readonly) NSString * _Nullable comment __attribute__((swift_name("comment")));
@property (readonly) NSString * _Nullable detail __attribute__((swift_name("detail")));
@property (readonly) int32_t estimatedMinutes __attribute__((swift_name("estimatedMinutes")));
@property (readonly) NSString * _Nullable footnote __attribute__((swift_name("footnote")));
@property (readonly) NSArray<AssessmentModelButtonAction *> *hideButtons __attribute__((swift_name("hideButtons")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) id<AssessmentModelImageInfo> _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@property (readonly) NSString * _Nullable schemaIdentifier __attribute__((swift_name("schemaIdentifier")));
@property (readonly) NSString * _Nullable subtitle __attribute__((swift_name("subtitle")));
@property (readonly) NSString * _Nullable title __attribute__((swift_name("title")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((swift_name("IconNodeObject")))
@interface AssessmentModelIconNodeObject : AssessmentModelNodeObject
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)doCopyFromOriginal:(id<AssessmentModelContentNode>)original __attribute__((swift_name("doCopyFrom(original:)")));
@property AssessmentModelFetchableImage * _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@end;

__attribute__((swift_name("NodeContainerObject")))
@interface AssessmentModelNodeContainerObject : AssessmentModelIconNodeObject <AssessmentModelNodeContainer>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)doCopyFromOriginal:(id<AssessmentModelContentNode>)original __attribute__((swift_name("doCopyFrom(original:)")));
@property NSArray<NSString *> * _Nullable progressMarkers __attribute__((swift_name("progressMarkers")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentObject")))
@interface AssessmentModelAssessmentObject : AssessmentModelNodeContainerObject <AssessmentModelAssessment, AssessmentModelAsyncActionContainer>
- (instancetype)initWithIdentifier:(NSString *)identifier children:(NSArray<id<AssessmentModelNode>> *)children versionString:(NSString * _Nullable)versionString schemaIdentifier:(NSString * _Nullable)schemaIdentifier estimatedMinutes:(int32_t)estimatedMinutes backgroundActions:(NSArray<id<AssessmentModelAsyncActionConfiguration>> *)backgroundActions __attribute__((swift_name("init(identifier:children:versionString:schemaIdentifier:estimatedMinutes:backgroundActions:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSArray<id<AssessmentModelNode>> *)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (int32_t)component5 __attribute__((swift_name("component5()")));
- (NSArray<id<AssessmentModelAsyncActionConfiguration>> *)component6 __attribute__((swift_name("component6()")));
- (AssessmentModelAssessmentObject *)doCopyIdentifier:(NSString *)identifier children:(NSArray<id<AssessmentModelNode>> *)children versionString:(NSString * _Nullable)versionString schemaIdentifier:(NSString * _Nullable)schemaIdentifier estimatedMinutes:(int32_t)estimatedMinutes backgroundActions:(NSArray<id<AssessmentModelAsyncActionConfiguration>> *)backgroundActions __attribute__((swift_name("doCopy(identifier:children:versionString:schemaIdentifier:estimatedMinutes:backgroundActions:)")));
- (id<AssessmentModelAssessmentResult>)createResult __attribute__((swift_name("createResult()")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
- (AssessmentModelAssessmentObject *)unpackFileLoader:(id<AssessmentModelFileLoader>)fileLoader resourceInfo:(id<AssessmentModelResourceInfo>)resourceInfo jsonCoder:(AssessmentModelKotlinx_serialization_jsonJson *)jsonCoder __attribute__((swift_name("unpack(fileLoader:resourceInfo:jsonCoder:)")));
@property (readonly) NSArray<id<AssessmentModelAsyncActionConfiguration>> *backgroundActions __attribute__((swift_name("backgroundActions")));
@property (readonly) NSArray<id<AssessmentModelNode>> *children __attribute__((swift_name("children")));
@property int32_t estimatedMinutes __attribute__((swift_name("estimatedMinutes")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) NSString * _Nullable schemaIdentifier __attribute__((swift_name("schemaIdentifier")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentObject.Companion")))
@interface AssessmentModelAssessmentObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("ResultNavigationRule")))
@protocol AssessmentModelResultNavigationRule <AssessmentModelDirectNavigationRule, AssessmentModelResult>
@required
- (void)setNextNodeIdentifier:(NSString * _Nullable)value __attribute__((swift_name("setNextNodeIdentifier(_:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentResultObject")))
@interface AssessmentModelAssessmentResultObject : AssessmentModelBase <AssessmentModelAssessmentResult, AssessmentModelResultNavigationRule>
- (instancetype)initWithIdentifier:(NSString *)identifier assessmentIdentifier:(NSString * _Nullable)assessmentIdentifier schemaIdentifier:(NSString * _Nullable)schemaIdentifier versionString:(NSString * _Nullable)versionString pathHistoryResults:(NSMutableArray<id<AssessmentModelResult>> *)pathHistoryResults inputResults:(AssessmentModelMutableSet<id<AssessmentModelResult>> *)inputResults runUUIDString:(NSString *)runUUIDString startDateString:(NSString *)startDateString endDateString:(NSString * _Nullable)endDateString path:(NSMutableArray<AssessmentModelPathMarker *> *)path nextNodeIdentifier:(NSString * _Nullable)nextNodeIdentifier __attribute__((swift_name("init(identifier:assessmentIdentifier:schemaIdentifier:versionString:pathHistoryResults:inputResults:runUUIDString:startDateString:endDateString:path:nextNodeIdentifier:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSMutableArray<AssessmentModelPathMarker *> *)component10 __attribute__((swift_name("component10()")));
- (NSString * _Nullable)component11 __attribute__((swift_name("component11()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSMutableArray<id<AssessmentModelResult>> *)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelMutableSet<id<AssessmentModelResult>> *)component6 __attribute__((swift_name("component6()")));
- (NSString *)component7 __attribute__((swift_name("component7()")));
- (NSString *)component8 __attribute__((swift_name("component8()")));
- (NSString * _Nullable)component9 __attribute__((swift_name("component9()")));
- (AssessmentModelAssessmentResultObject *)doCopyIdentifier:(NSString *)identifier assessmentIdentifier:(NSString * _Nullable)assessmentIdentifier schemaIdentifier:(NSString * _Nullable)schemaIdentifier versionString:(NSString * _Nullable)versionString pathHistoryResults:(NSMutableArray<id<AssessmentModelResult>> *)pathHistoryResults inputResults:(AssessmentModelMutableSet<id<AssessmentModelResult>> *)inputResults runUUIDString:(NSString *)runUUIDString startDateString:(NSString *)startDateString endDateString:(NSString * _Nullable)endDateString path:(NSMutableArray<AssessmentModelPathMarker *> *)path nextNodeIdentifier:(NSString * _Nullable)nextNodeIdentifier __attribute__((swift_name("doCopy(identifier:assessmentIdentifier:schemaIdentifier:versionString:pathHistoryResults:inputResults:runUUIDString:startDateString:endDateString:path:nextNodeIdentifier:)")));
- (id<AssessmentModelAssessmentResult>)doCopyResultIdentifier:(NSString *)identifier __attribute__((swift_name("doCopyResult(identifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable assessmentIdentifier __attribute__((swift_name("assessmentIdentifier")));
@property NSString * _Nullable endDateString __attribute__((swift_name("endDateString")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property AssessmentModelMutableSet<id<AssessmentModelResult>> *inputResults __attribute__((swift_name("inputResults")));
@property NSString * _Nullable nextNodeIdentifier __attribute__((swift_name("nextNodeIdentifier")));
@property (readonly) NSMutableArray<AssessmentModelPathMarker *> *path __attribute__((swift_name("path")));
@property NSMutableArray<id<AssessmentModelResult>> *pathHistoryResults __attribute__((swift_name("pathHistoryResults")));
@property NSString *runUUIDString __attribute__((swift_name("runUUIDString")));
@property (readonly) NSString * _Nullable schemaIdentifier __attribute__((swift_name("schemaIdentifier")));
@property NSString *startDateString __attribute__((swift_name("startDateString")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentResultObject.Companion")))
@interface AssessmentModelAssessmentResultObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BaseActiveStepObject.Companion")))
@interface AssessmentModelBaseActiveStepObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BranchNodeResultObject")))
@interface AssessmentModelBranchNodeResultObject : AssessmentModelBase <AssessmentModelBranchNodeResult, AssessmentModelResultNavigationRule>
- (instancetype)initWithIdentifier:(NSString *)identifier pathHistoryResults:(NSMutableArray<id<AssessmentModelResult>> *)pathHistoryResults inputResults:(AssessmentModelMutableSet<id<AssessmentModelResult>> *)inputResults startDateString:(NSString *)startDateString endDateString:(NSString * _Nullable)endDateString path:(NSMutableArray<AssessmentModelPathMarker *> *)path nextNodeIdentifier:(NSString * _Nullable)nextNodeIdentifier __attribute__((swift_name("init(identifier:pathHistoryResults:inputResults:startDateString:endDateString:path:nextNodeIdentifier:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSMutableArray<id<AssessmentModelResult>> *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelMutableSet<id<AssessmentModelResult>> *)component3 __attribute__((swift_name("component3()")));
- (NSString *)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (NSMutableArray<AssessmentModelPathMarker *> *)component6 __attribute__((swift_name("component6()")));
- (NSString * _Nullable)component7 __attribute__((swift_name("component7()")));
- (AssessmentModelBranchNodeResultObject *)doCopyIdentifier:(NSString *)identifier pathHistoryResults:(NSMutableArray<id<AssessmentModelResult>> *)pathHistoryResults inputResults:(AssessmentModelMutableSet<id<AssessmentModelResult>> *)inputResults startDateString:(NSString *)startDateString endDateString:(NSString * _Nullable)endDateString path:(NSMutableArray<AssessmentModelPathMarker *> *)path nextNodeIdentifier:(NSString * _Nullable)nextNodeIdentifier __attribute__((swift_name("doCopy(identifier:pathHistoryResults:inputResults:startDateString:endDateString:path:nextNodeIdentifier:)")));
- (id<AssessmentModelBranchNodeResult>)doCopyResultIdentifier:(NSString *)identifier __attribute__((swift_name("doCopyResult(identifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSString * _Nullable endDateString __attribute__((swift_name("endDateString")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property AssessmentModelMutableSet<id<AssessmentModelResult>> *inputResults __attribute__((swift_name("inputResults")));
@property NSString * _Nullable nextNodeIdentifier __attribute__((swift_name("nextNodeIdentifier")));
@property (readonly) NSMutableArray<AssessmentModelPathMarker *> *path __attribute__((swift_name("path")));
@property NSMutableArray<id<AssessmentModelResult>> *pathHistoryResults __attribute__((swift_name("pathHistoryResults")));
@property NSString *startDateString __attribute__((swift_name("startDateString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BranchNodeResultObject.Companion")))
@interface AssessmentModelBranchNodeResultObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("SerializableButtonActionInfo")))
@interface AssessmentModelSerializableButtonActionInfo : AssessmentModelBase <AssessmentModelButtonActionInfo, AssessmentModelResourceInfo>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@property id _Nullable decoderBundle __attribute__((swift_name("decoderBundle")));
@property (readonly) NSString * _Nullable iconName __attribute__((swift_name("iconName")));
@property (readonly) id<AssessmentModelImageInfo> _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonActionInfoObject")))
@interface AssessmentModelButtonActionInfoObject : AssessmentModelSerializableButtonActionInfo
- (instancetype)initWithButtonTitle:(NSString * _Nullable)buttonTitle iconName:(NSString * _Nullable)iconName packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("init(buttonTitle:iconName:packageName:bundleIdentifier:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelButtonActionInfoObject *)doCopyButtonTitle:(NSString * _Nullable)buttonTitle iconName:(NSString * _Nullable)iconName packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("doCopy(buttonTitle:iconName:packageName:bundleIdentifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property (readonly) NSString * _Nullable buttonTitle __attribute__((swift_name("buttonTitle")));
@property (readonly) NSString * _Nullable iconName __attribute__((swift_name("iconName")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonActionInfoObject.Companion")))
@interface AssessmentModelButtonActionInfoObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CheckboxInputItemObject")))
@interface AssessmentModelCheckboxInputItemObject : AssessmentModelBase <AssessmentModelCheckboxInputItem>
- (instancetype)initWithResultIdentifier:(NSString *)resultIdentifier fieldLabel:(NSString *)fieldLabel __attribute__((swift_name("init(resultIdentifier:fieldLabel:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelCheckboxInputItemObject *)doCopyResultIdentifier:(NSString *)resultIdentifier fieldLabel:(NSString *)fieldLabel __attribute__((swift_name("doCopy(resultIdentifier:fieldLabel:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *fieldLabel __attribute__((swift_name("fieldLabel")));
@property (readonly) NSString *resultIdentifier __attribute__((swift_name("resultIdentifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CheckboxInputItemObject.Companion")))
@interface AssessmentModelCheckboxInputItemObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ChoiceItemWrapper")))
@interface AssessmentModelChoiceItemWrapper : AssessmentModelBase <AssessmentModelChoiceInputItem, AssessmentModelChoiceOption>
- (instancetype)initWithChoice:(id<AssessmentModelChoiceOption>)choice singleChoice:(BOOL)singleChoice answerType:(AssessmentModelAnswerType *)answerType uiHint:(AssessmentModelUIHint *)uiHint __attribute__((swift_name("init(choice:singleChoice:answerType:uiHint:)"))) __attribute__((objc_designated_initializer));
- (id<AssessmentModelChoiceOption>)component1 __attribute__((swift_name("component1()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelAnswerType *)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelUIHint *)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelChoiceItemWrapper *)doCopyChoice:(id<AssessmentModelChoiceOption>)choice singleChoice:(BOOL)singleChoice answerType:(AssessmentModelAnswerType *)answerType uiHint:(AssessmentModelUIHint *)uiHint __attribute__((swift_name("doCopy(choice:singleChoice:answerType:uiHint:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValueSelected:(BOOL)selected __attribute__((swift_name("jsonValue(selected:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelAnswerType *answerType __attribute__((swift_name("answerType")));
@property (readonly) id<AssessmentModelChoiceOption> choice __attribute__((swift_name("choice")));
@property (readonly) NSString * _Nullable detail __attribute__((swift_name("detail")));
@property (readonly) BOOL exclusive __attribute__((swift_name("exclusive")));
@property (readonly) NSString * _Nullable fieldLabel __attribute__((swift_name("fieldLabel")));
@property (readonly) AssessmentModelFetchableImage * _Nullable icon __attribute__((swift_name("icon")));
@property (readonly) NSString * _Nullable resultIdentifier __attribute__((swift_name("resultIdentifier")));
@property (readonly) BOOL singleChoice __attribute__((swift_name("singleChoice")));
@property (readonly) AssessmentModelUIHint *uiHint __attribute__((swift_name("uiHint")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ChoiceOptionObject")))
@interface AssessmentModelChoiceOptionObject : AssessmentModelBase <AssessmentModelChoiceOption>
- (instancetype)initWithValue:(AssessmentModelKotlinx_serialization_jsonJsonElement *)value fieldLabel:(NSString * _Nullable)fieldLabel icon:(AssessmentModelFetchableImage * _Nullable)icon exclusive:(BOOL)exclusive detail:(NSString * _Nullable)detail __attribute__((swift_name("init(value:fieldLabel:icon:exclusive:detail:)"))) __attribute__((objc_designated_initializer));
- (AssessmentModelKotlinx_serialization_jsonJsonElement *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelFetchableImage * _Nullable)component3 __attribute__((swift_name("component3()")));
- (BOOL)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelChoiceOptionObject *)doCopyValue:(AssessmentModelKotlinx_serialization_jsonJsonElement *)value fieldLabel:(NSString * _Nullable)fieldLabel icon:(AssessmentModelFetchableImage * _Nullable)icon exclusive:(BOOL)exclusive detail:(NSString * _Nullable)detail __attribute__((swift_name("doCopy(value:fieldLabel:icon:exclusive:detail:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValueSelected:(BOOL)selected __attribute__((swift_name("jsonValue(selected:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable detail __attribute__((swift_name("detail")));
@property (readonly) BOOL exclusive __attribute__((swift_name("exclusive")));
@property (readonly) NSString * _Nullable fieldLabel __attribute__((swift_name("fieldLabel")));
@property (readonly) AssessmentModelFetchableImage * _Nullable icon __attribute__((swift_name("icon")));
@property (readonly) AssessmentModelKotlinx_serialization_jsonJsonElement *value __attribute__((swift_name("value")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ChoiceOptionObject.Companion")))
@interface AssessmentModelChoiceOptionObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("SurveyNavigationRule")))
@protocol AssessmentModelSurveyNavigationRule <AssessmentModelDirectNavigationRule, AssessmentModelResultMapElement>
@required
@property (readonly) NSArray<id<AssessmentModelSurveyRule>> * _Nullable surveyRules __attribute__((swift_name("surveyRules")));
@end;

__attribute__((swift_name("QuestionObject")))
@interface AssessmentModelQuestionObject : AssessmentModelStepObject <AssessmentModelQuestion, AssessmentModelSurveyNavigationRule>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)doCopyFromOriginal:(id<AssessmentModelContentNode>)original __attribute__((swift_name("doCopyFrom(original:)")));
@property id<AssessmentModelImageInfo> _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@property BOOL optional __attribute__((swift_name("optional")));
@property NSArray<AssessmentModelComparableSurveyRuleObject *> * _Nullable surveyRules __attribute__((swift_name("surveyRules")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ChoiceQuestionObject")))
@interface AssessmentModelChoiceQuestionObject : AssessmentModelQuestionObject <AssessmentModelChoiceQuestion>
- (instancetype)initWithIdentifier:(NSString *)identifier choices:(NSArray<AssessmentModelChoiceOptionObject *> *)choices baseType:(AssessmentModelBaseType *)baseType singleAnswer:(BOOL)singleAnswer uiHint:(AssessmentModelUIHint *)uiHint __attribute__((swift_name("init(identifier:choices:baseType:singleAnswer:uiHint:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSArray<AssessmentModelChoiceOptionObject *> *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelBaseType *)component3 __attribute__((swift_name("component3()")));
- (BOOL)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelUIHint *)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelChoiceQuestionObject *)doCopyIdentifier:(NSString *)identifier choices:(NSArray<AssessmentModelChoiceOptionObject *> *)choices baseType:(AssessmentModelBaseType *)baseType singleAnswer:(BOOL)singleAnswer uiHint:(AssessmentModelUIHint *)uiHint __attribute__((swift_name("doCopy(identifier:choices:baseType:singleAnswer:uiHint:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@property (readonly) NSArray<AssessmentModelChoiceOptionObject *> *choices __attribute__((swift_name("choices")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property BOOL singleAnswer __attribute__((swift_name("singleAnswer")));
@property AssessmentModelUIHint *uiHint __attribute__((swift_name("uiHint")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ChoiceQuestionObject.Companion")))
@interface AssessmentModelChoiceQuestionObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CollectionResultObject")))
@interface AssessmentModelCollectionResultObject : AssessmentModelBase <AssessmentModelCollectionResult, AssessmentModelResultNavigationRule>
- (instancetype)initWithIdentifier:(NSString *)identifier inputResults:(AssessmentModelMutableSet<id<AssessmentModelResult>> *)inputResults startDateString:(NSString *)startDateString endDateString:(NSString * _Nullable)endDateString nextNodeIdentifier:(NSString * _Nullable)nextNodeIdentifier __attribute__((swift_name("init(identifier:inputResults:startDateString:endDateString:nextNodeIdentifier:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelMutableSet<id<AssessmentModelResult>> *)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelCollectionResultObject *)doCopyIdentifier:(NSString *)identifier inputResults:(AssessmentModelMutableSet<id<AssessmentModelResult>> *)inputResults startDateString:(NSString *)startDateString endDateString:(NSString * _Nullable)endDateString nextNodeIdentifier:(NSString * _Nullable)nextNodeIdentifier __attribute__((swift_name("doCopy(identifier:inputResults:startDateString:endDateString:nextNodeIdentifier:)")));
- (id<AssessmentModelCollectionResult>)doCopyResultIdentifier:(NSString *)identifier __attribute__((swift_name("doCopyResult(identifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSString * _Nullable endDateString __attribute__((swift_name("endDateString")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property AssessmentModelMutableSet<id<AssessmentModelResult>> *inputResults __attribute__((swift_name("inputResults")));
@property NSString * _Nullable nextNodeIdentifier __attribute__((swift_name("nextNodeIdentifier")));
@property NSString *startDateString __attribute__((swift_name("startDateString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CollectionResultObject.Companion")))
@interface AssessmentModelCollectionResultObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ComboBoxQuestionObject")))
@interface AssessmentModelComboBoxQuestionObject : AssessmentModelQuestionObject <AssessmentModelComboBoxQuestion>
- (instancetype)initWithIdentifier:(NSString *)identifier choices:(NSArray<AssessmentModelChoiceOptionObject *> *)choices otherInputItem:(id<AssessmentModelInputItem>)otherInputItem singleAnswer:(BOOL)singleAnswer uiHint:(AssessmentModelUIHint *)uiHint __attribute__((swift_name("init(identifier:choices:otherInputItem:singleAnswer:uiHint:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSArray<AssessmentModelChoiceOptionObject *> *)component2 __attribute__((swift_name("component2()")));
- (id<AssessmentModelInputItem>)component3 __attribute__((swift_name("component3()")));
- (BOOL)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelUIHint *)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelComboBoxQuestionObject *)doCopyIdentifier:(NSString *)identifier choices:(NSArray<AssessmentModelChoiceOptionObject *> *)choices otherInputItem:(id<AssessmentModelInputItem>)otherInputItem singleAnswer:(BOOL)singleAnswer uiHint:(AssessmentModelUIHint *)uiHint __attribute__((swift_name("doCopy(identifier:choices:otherInputItem:singleAnswer:uiHint:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<AssessmentModelChoiceOptionObject *> *choices __attribute__((swift_name("choices")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) id<AssessmentModelInputItem> otherInputItem __attribute__((swift_name("otherInputItem")));
@property BOOL singleAnswer __attribute__((swift_name("singleAnswer")));
@property AssessmentModelUIHint *uiHint __attribute__((swift_name("uiHint")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ComboBoxQuestionObject.Companion")))
@interface AssessmentModelComboBoxQuestionObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) AssessmentModelStringTextInputItemObject *defaultOtherInputItem __attribute__((swift_name("defaultOtherInputItem")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ComparableSurveyRuleObject")))
@interface AssessmentModelComparableSurveyRuleObject : AssessmentModelBase <AssessmentModelComparableSurveyRule>
- (instancetype)initWithMatchingAnswer:(AssessmentModelKotlinx_serialization_jsonJsonElement *)matchingAnswer skipToIdentifier:(NSString * _Nullable)skipToIdentifier ruleOperator:(AssessmentModelSurveyRuleOperator * _Nullable)ruleOperator accuracy:(double)accuracy __attribute__((swift_name("init(matchingAnswer:skipToIdentifier:ruleOperator:accuracy:)"))) __attribute__((objc_designated_initializer));
- (AssessmentModelKotlinx_serialization_jsonJsonElement *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelSurveyRuleOperator * _Nullable)component3 __attribute__((swift_name("component3()")));
- (double)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelComparableSurveyRuleObject *)doCopyMatchingAnswer:(AssessmentModelKotlinx_serialization_jsonJsonElement *)matchingAnswer skipToIdentifier:(NSString * _Nullable)skipToIdentifier ruleOperator:(AssessmentModelSurveyRuleOperator * _Nullable)ruleOperator accuracy:(double)accuracy __attribute__((swift_name("doCopy(matchingAnswer:skipToIdentifier:ruleOperator:accuracy:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double accuracy __attribute__((swift_name("accuracy")));
@property (readonly) AssessmentModelKotlinx_serialization_jsonJsonElement *matchingAnswer __attribute__((swift_name("matchingAnswer")));
@property (readonly) AssessmentModelSurveyRuleOperator * _Nullable ruleOperator __attribute__((swift_name("ruleOperator")));
@property (readonly) NSString * _Nullable skipToIdentifier __attribute__((swift_name("skipToIdentifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ComparableSurveyRuleObject.Companion")))
@interface AssessmentModelComparableSurveyRuleObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CountdownStepObject")))
@interface AssessmentModelCountdownStepObject : AssessmentModelBaseActiveStepObject <AssessmentModelCountdownStep>
- (instancetype)initWithIdentifier:(NSString *)identifier duration:(double)duration fullInstructionsOnly:(BOOL)fullInstructionsOnly __attribute__((swift_name("init(identifier:duration:fullInstructionsOnly:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (double)component2 __attribute__((swift_name("component2()")));
- (BOOL)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelCountdownStepObject *)doCopyIdentifier:(NSString *)identifier duration:(double)duration fullInstructionsOnly:(BOOL)fullInstructionsOnly __attribute__((swift_name("doCopy(identifier:duration:fullInstructionsOnly:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSSet<AssessmentModelActiveStepCommand *> *commands __attribute__((swift_name("commands")));
@property (readonly) double duration __attribute__((swift_name("duration")));
@property (readonly) BOOL fullInstructionsOnly __attribute__((swift_name("fullInstructionsOnly")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CountdownStepObject.Companion")))
@interface AssessmentModelCountdownStepObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateFormatOptions")))
@interface AssessmentModelDateFormatOptions : AssessmentModelBase <AssessmentModelDateTimeFormatOptions>
- (instancetype)initWithAllowFuture:(BOOL)allowFuture allowPast:(BOOL)allowPast minimumValue:(NSString * _Nullable)minimumValue maximumValue:(NSString * _Nullable)maximumValue codingFormat:(NSString *)codingFormat __attribute__((swift_name("init(allowFuture:allowPast:minimumValue:maximumValue:codingFormat:)"))) __attribute__((objc_designated_initializer));
- (BOOL)component1 __attribute__((swift_name("component1()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString *)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelDateFormatOptions *)doCopyAllowFuture:(BOOL)allowFuture allowPast:(BOOL)allowPast minimumValue:(NSString * _Nullable)minimumValue maximumValue:(NSString * _Nullable)maximumValue codingFormat:(NSString *)codingFormat __attribute__((swift_name("doCopy(allowFuture:allowPast:minimumValue:maximumValue:codingFormat:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL allowFuture __attribute__((swift_name("allowFuture")));
@property (readonly) BOOL allowPast __attribute__((swift_name("allowPast")));
@property (readonly) NSString *codingFormat __attribute__((swift_name("codingFormat")));
@property (readonly) NSString * _Nullable maximumValue __attribute__((swift_name("maximumValue")));
@property (readonly) NSString * _Nullable minimumValue __attribute__((swift_name("minimumValue")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateFormatOptions.Companion")))
@interface AssessmentModelDateFormatOptionsCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("InputItemObject")))
@interface AssessmentModelInputItemObject : AssessmentModelBase <AssessmentModelInputItem>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@property BOOL exclusive __attribute__((swift_name("exclusive")));
@property NSString * _Nullable fieldLabel __attribute__((swift_name("fieldLabel")));
@property BOOL optional __attribute__((swift_name("optional")));
@property NSString * _Nullable placeholder __attribute__((swift_name("placeholder")));
@property AssessmentModelUIHintTextField *uiHint __attribute__((swift_name("uiHint")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateInputItemObject")))
@interface AssessmentModelDateInputItemObject : AssessmentModelInputItemObject <AssessmentModelDateTimeInputItem>
- (instancetype)initWithResultIdentifier:(NSString * _Nullable)resultIdentifier formatOptions:(AssessmentModelDateFormatOptions *)formatOptions __attribute__((swift_name("init(resultIdentifier:formatOptions:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelDateFormatOptions *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelDateInputItemObject *)doCopyResultIdentifier:(NSString * _Nullable)resultIdentifier formatOptions:(AssessmentModelDateFormatOptions *)formatOptions __attribute__((swift_name("doCopy(resultIdentifier:formatOptions:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property AssessmentModelDateFormatOptions *formatOptions __attribute__((swift_name("formatOptions")));
@property (readonly) NSString * _Nullable resultIdentifier __attribute__((swift_name("resultIdentifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateInputItemObject.Companion")))
@interface AssessmentModelDateInputItemObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DecimalTextInputItemObject")))
@interface AssessmentModelDecimalTextInputItemObject : AssessmentModelInputItemObject <AssessmentModelKeyboardTextInputItem>
- (instancetype)initWithResultIdentifier:(NSString * _Nullable)resultIdentifier formatOptions:(AssessmentModelDoubleFormatOptions *)formatOptions __attribute__((swift_name("init(resultIdentifier:formatOptions:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (id<AssessmentModelTextValidator>)buildTextValidator __attribute__((swift_name("buildTextValidator()")));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelDoubleFormatOptions *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelDecimalTextInputItemObject *)doCopyResultIdentifier:(NSString * _Nullable)resultIdentifier formatOptions:(AssessmentModelDoubleFormatOptions *)formatOptions __attribute__((swift_name("doCopy(resultIdentifier:formatOptions:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelAnswerType *answerType __attribute__((swift_name("answerType")));
@property AssessmentModelDoubleFormatOptions *formatOptions __attribute__((swift_name("formatOptions")));
@property (readonly) id<AssessmentModelKeyboardOptions> keyboardOptions __attribute__((swift_name("keyboardOptions")));
@property (readonly) NSString * _Nullable resultIdentifier __attribute__((swift_name("resultIdentifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DecimalTextInputItemObject.Companion")))
@interface AssessmentModelDecimalTextInputItemObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("NumberFormatOptions")))
@interface AssessmentModelNumberFormatOptions<T> : AssessmentModelBase <AssessmentModelNumberRange>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
@property AssessmentModelInvalidMessageObject *invalidMessage __attribute__((swift_name("invalidMessage")));
@property AssessmentModelInvalidMessageObject * _Nullable maxInvalidMessage __attribute__((swift_name("maxInvalidMessage")));
@property (readonly) int32_t maximumFractionDigits __attribute__((swift_name("maximumFractionDigits")));
@property AssessmentModelInvalidMessageObject * _Nullable minInvalidMessage __attribute__((swift_name("minInvalidMessage")));
@property (readonly) AssessmentModelNumberFormatOptionsStyle *numberStyle __attribute__((swift_name("numberStyle")));
@property (readonly) BOOL usesGroupingSeparator __attribute__((swift_name("usesGroupingSeparator")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DoubleFormatOptions")))
@interface AssessmentModelDoubleFormatOptions : AssessmentModelNumberFormatOptions<AssessmentModelDouble *>
- (instancetype)initWithNumberStyle:(AssessmentModelNumberFormatOptionsStyle *)numberStyle usesGroupingSeparator:(BOOL)usesGroupingSeparator maximumFractionDigits:(int32_t)maximumFractionDigits __attribute__((swift_name("init(numberStyle:usesGroupingSeparator:maximumFractionDigits:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (AssessmentModelNumberFormatOptionsStyle *)component1 __attribute__((swift_name("component1()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (int32_t)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelDoubleFormatOptions *)doCopyNumberStyle:(AssessmentModelNumberFormatOptionsStyle *)numberStyle usesGroupingSeparator:(BOOL)usesGroupingSeparator maximumFractionDigits:(int32_t)maximumFractionDigits __attribute__((swift_name("doCopy(numberStyle:usesGroupingSeparator:maximumFractionDigits:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t maximumFractionDigits __attribute__((swift_name("maximumFractionDigits")));
@property AssessmentModelDouble * _Nullable maximumValue __attribute__((swift_name("maximumValue")));
@property AssessmentModelDouble * _Nullable minimumValue __attribute__((swift_name("minimumValue")));
@property (readonly) AssessmentModelNumberFormatOptionsStyle *numberStyle __attribute__((swift_name("numberStyle")));
@property (readonly) AssessmentModelNumberType *numberType __attribute__((swift_name("numberType")));
@property AssessmentModelDouble * _Nullable stepInterval __attribute__((swift_name("stepInterval")));
@property (readonly) BOOL usesGroupingSeparator __attribute__((swift_name("usesGroupingSeparator")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DoubleFormatOptions.Companion")))
@interface AssessmentModelDoubleFormatOptionsCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("NumberFormatter")))
@interface AssessmentModelNumberFormatter<T> : AssessmentModelBase <AssessmentModelTextValidator>
- (instancetype)initWithFormatOptions:(AssessmentModelNumberFormatOptions<T> *)formatOptions __attribute__((swift_name("init(formatOptions:)"))) __attribute__((objc_designated_initializer));
- (AssessmentModelFormattedValue<NSString *> *)localizedStringForValue:(T _Nullable)value __attribute__((swift_name("localizedStringFor(value:)")));
- (NSNumber *)toNSNumberValue:(T)value __attribute__((swift_name("toNSNumber(value:)")));
- (T _Nullable)toTypeValue:(NSNumber * _Nullable)value __attribute__((swift_name("toType(value:)")));
- (AssessmentModelFormattedValue<T> * _Nullable)valueForText:(NSString *)text __attribute__((swift_name("valueFor(text:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DoubleFormatter")))
@interface AssessmentModelDoubleFormatter : AssessmentModelNumberFormatter<AssessmentModelDouble *>
- (instancetype)initWithFormatOptions:(AssessmentModelNumberFormatOptions<AssessmentModelDouble *> *)formatOptions __attribute__((swift_name("init(formatOptions:)"))) __attribute__((objc_designated_initializer));
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValueForValue:(AssessmentModelDouble * _Nullable)value __attribute__((swift_name("jsonValueFor(value:)")));
- (NSNumber *)toNSNumberValue:(AssessmentModelDouble *)value __attribute__((swift_name("toNSNumber(value:)")));
- (AssessmentModelDouble * _Nullable)toTypeValue:(NSNumber * _Nullable)value __attribute__((swift_name("toType(value:)")));
- (AssessmentModelDouble * _Nullable)valueForJsonValue:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValue __attribute__((swift_name("valueFor(jsonValue:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FetchableImage")))
@interface AssessmentModelFetchableImage : AssessmentModelBase <AssessmentModelImageInfo, AssessmentModelImageTheme>
- (instancetype)initWithImageName:(NSString *)imageName label:(NSString * _Nullable)label imagePlacement:(AssessmentModelImagePlacement * _Nullable)imagePlacement size:(AssessmentModelSize * _Nullable)size decoderBundle:(id _Nullable)decoderBundle bundleIdentifier:(NSString * _Nullable)bundleIdentifier packageName:(NSString * _Nullable)packageName rawFileExtension:(NSString * _Nullable)rawFileExtension versionString:(NSString * _Nullable)versionString __attribute__((swift_name("init(imageName:label:imagePlacement:size:decoderBundle:bundleIdentifier:packageName:rawFileExtension:versionString:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelImagePlacement * _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelSize * _Nullable)component4 __attribute__((swift_name("component4()")));
- (id _Nullable)component5 __attribute__((swift_name("component5()")));
- (NSString * _Nullable)component6 __attribute__((swift_name("component6()")));
- (NSString * _Nullable)component7 __attribute__((swift_name("component7()")));
- (NSString * _Nullable)component8 __attribute__((swift_name("component8()")));
- (NSString * _Nullable)component9 __attribute__((swift_name("component9()")));
- (AssessmentModelFetchableImage *)doCopyImageName:(NSString *)imageName label:(NSString * _Nullable)label imagePlacement:(AssessmentModelImagePlacement * _Nullable)imagePlacement size:(AssessmentModelSize * _Nullable)size decoderBundle:(id _Nullable)decoderBundle bundleIdentifier:(NSString * _Nullable)bundleIdentifier packageName:(NSString * _Nullable)packageName rawFileExtension:(NSString * _Nullable)rawFileExtension versionString:(NSString * _Nullable)versionString __attribute__((swift_name("doCopy(imageName:label:imagePlacement:size:decoderBundle:bundleIdentifier:packageName:rawFileExtension:versionString:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property id _Nullable decoderBundle __attribute__((swift_name("decoderBundle")));
@property (readonly) NSString *imageName __attribute__((swift_name("imageName")));
@property (readonly) AssessmentModelImagePlacement * _Nullable imagePlacement __attribute__((swift_name("imagePlacement")));
@property (readonly) NSString * _Nullable label __attribute__((swift_name("label")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@property (readonly) NSString * _Nullable rawFileExtension __attribute__((swift_name("rawFileExtension")));
@property (readonly) AssessmentModelSize * _Nullable size __attribute__((swift_name("size")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FetchableImage.Companion")))
@interface AssessmentModelFetchableImageCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("ResourceAssessmentProvider")))
@protocol AssessmentModelResourceAssessmentProvider <AssessmentModelAssessmentGroupInfo, AssessmentModelAssessmentProvider>
@required
@property (readonly) id<AssessmentModelFileLoader> fileLoader __attribute__((swift_name("fileLoader")));
@property (readonly) AssessmentModelKotlinx_serialization_jsonJson *jsonCoder __attribute__((swift_name("jsonCoder")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FileAssessmentProvider")))
@interface AssessmentModelFileAssessmentProvider : AssessmentModelBase <AssessmentModelResourceAssessmentProvider, AssessmentModelAssessmentGroupInfo>
- (instancetype)initWithFileLoader:(id<AssessmentModelFileLoader>)fileLoader assessmentGroupInfo:(id<AssessmentModelAssessmentGroupInfo>)assessmentGroupInfo jsonCoder:(AssessmentModelKotlinx_serialization_jsonJson *)jsonCoder __attribute__((swift_name("init(fileLoader:assessmentGroupInfo:jsonCoder:)"))) __attribute__((objc_designated_initializer));
@property (readonly) NSArray<id<AssessmentModelAssessment>> *assessments __attribute__((swift_name("assessments")));
@property (readonly) id<AssessmentModelFileLoader> fileLoader __attribute__((swift_name("fileLoader")));
@property AssessmentModelKotlinx_serialization_jsonJson *jsonCoder __attribute__((swift_name("jsonCoder")));
@property (readonly) id<AssessmentModelResourceInfo> resourceInfo __attribute__((swift_name("resourceInfo")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FileLoaderIOS")))
@interface AssessmentModelFileLoaderIOS : AssessmentModelBase <AssessmentModelFileLoader>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (NSString *)loadFileAssetInfo:(id<AssessmentModelAssetInfo>)assetInfo resourceInfo:(id<AssessmentModelResourceInfo>)resourceInfo __attribute__((swift_name("loadFile(assetInfo:resourceInfo:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FormStepObject")))
@interface AssessmentModelFormStepObject : AssessmentModelStepObject <AssessmentModelFormStep>
- (instancetype)initWithIdentifier:(NSString *)identifier imageInfo:(id<AssessmentModelImageInfo> _Nullable)imageInfo children:(NSArray<id<AssessmentModelNode>> *)children __attribute__((swift_name("init(identifier:imageInfo:children:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (id<AssessmentModelImageInfo> _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSArray<id<AssessmentModelNode>> *)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelFormStepObject *)doCopyIdentifier:(NSString *)identifier imageInfo:(id<AssessmentModelImageInfo> _Nullable)imageInfo children:(NSArray<id<AssessmentModelNode>> *)children __attribute__((swift_name("doCopy(identifier:imageInfo:children:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<id<AssessmentModelNode>> *children __attribute__((swift_name("children")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) id<AssessmentModelImageInfo> _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FormStepObject.Companion")))
@interface AssessmentModelFormStepObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IconInfoObject")))
@interface AssessmentModelIconInfoObject : AssessmentModelBase <AssessmentModelImageInfo>
- (instancetype)initWithImageName:(NSString *)imageName label:(NSString * _Nullable)label bundleIdentifier:(NSString * _Nullable)bundleIdentifier packageName:(NSString * _Nullable)packageName rawFileExtension:(NSString * _Nullable)rawFileExtension decoderBundle:(id _Nullable)decoderBundle versionString:(NSString * _Nullable)versionString __attribute__((swift_name("init(imageName:label:bundleIdentifier:packageName:rawFileExtension:decoderBundle:versionString:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (id _Nullable)component6 __attribute__((swift_name("component6()")));
- (NSString * _Nullable)component7 __attribute__((swift_name("component7()")));
- (AssessmentModelIconInfoObject *)doCopyImageName:(NSString *)imageName label:(NSString * _Nullable)label bundleIdentifier:(NSString * _Nullable)bundleIdentifier packageName:(NSString * _Nullable)packageName rawFileExtension:(NSString * _Nullable)rawFileExtension decoderBundle:(id _Nullable)decoderBundle versionString:(NSString * _Nullable)versionString __attribute__((swift_name("doCopy(imageName:label:bundleIdentifier:packageName:rawFileExtension:decoderBundle:versionString:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property id _Nullable decoderBundle __attribute__((swift_name("decoderBundle")));
@property (readonly) NSString *imageName __attribute__((swift_name("imageName")));
@property (readonly) NSString * _Nullable label __attribute__((swift_name("label")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@property (readonly) NSString * _Nullable rawFileExtension __attribute__((swift_name("rawFileExtension")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IconInfoObject.Companion")))
@interface AssessmentModelIconInfoObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IconNodeObject.Companion")))
@interface AssessmentModelIconNodeObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImageNameSerializer")))
@interface AssessmentModelImageNameSerializer : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)imageNameSerializer __attribute__((swift_name("init()")));
- (AssessmentModelFetchableImage *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelFetchableImage *)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((swift_name("ImagePlacement")))
@interface AssessmentModelImagePlacement : AssessmentModelBase <AssessmentModelStringEnum>
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImagePlacement.Companion")))
@interface AssessmentModelImagePlacementCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelImagePlacement *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelImagePlacement *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (AssessmentModelImagePlacement *)valueOfName:(NSString *)name __attribute__((swift_name("valueOf(name:)")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImagePlacement.Custom")))
@interface AssessmentModelImagePlacementCustom : AssessmentModelImagePlacement
- (instancetype)initWithName:(NSString *)name __attribute__((swift_name("init(name:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelImagePlacementCustom *)doCopyName:(NSString *)name __attribute__((swift_name("doCopy(name:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((swift_name("ImagePlacement.Standard")))
@interface AssessmentModelImagePlacementStandard : AssessmentModelImagePlacement
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImagePlacement.StandardBackgroundAfter")))
@interface AssessmentModelImagePlacementStandardBackgroundAfter : AssessmentModelImagePlacementStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)backgroundAfter __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImagePlacement.StandardBackgroundBefore")))
@interface AssessmentModelImagePlacementStandardBackgroundBefore : AssessmentModelImagePlacementStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)backgroundBefore __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImagePlacement.StandardCompanion")))
@interface AssessmentModelImagePlacementStandardCompanion : AssessmentModelBase <AssessmentModelStringEnumCompanion>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelKotlinArray<AssessmentModelImagePlacementStandard *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImagePlacement.StandardFullSizeBackground")))
@interface AssessmentModelImagePlacementStandardFullSizeBackground : AssessmentModelImagePlacementStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)fullSizeBackground __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImagePlacement.StandardIconAfter")))
@interface AssessmentModelImagePlacementStandardIconAfter : AssessmentModelImagePlacementStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)iconAfter __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImagePlacement.StandardIconBefore")))
@interface AssessmentModelImagePlacementStandardIconBefore : AssessmentModelImagePlacementStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)iconBefore __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImagePlacement.StandardTopBackground")))
@interface AssessmentModelImagePlacementStandardTopBackground : AssessmentModelImagePlacementStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)topBackground __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImagePlacement.StandardTopMarginBackground")))
@interface AssessmentModelImagePlacementStandardTopMarginBackground : AssessmentModelImagePlacementStandard
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)topMarginBackground __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("InputItemObject.Companion")))
@interface AssessmentModelInputItemObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("InstructionStepObject")))
@interface AssessmentModelInstructionStepObject : AssessmentModelStepObject <AssessmentModelInstructionStep>
- (instancetype)initWithIdentifier:(NSString *)identifier imageInfo:(id<AssessmentModelImageInfo> _Nullable)imageInfo fullInstructionsOnly:(BOOL)fullInstructionsOnly __attribute__((swift_name("init(identifier:imageInfo:fullInstructionsOnly:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (id<AssessmentModelImageInfo> _Nullable)component2 __attribute__((swift_name("component2()")));
- (BOOL)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelInstructionStepObject *)doCopyIdentifier:(NSString *)identifier imageInfo:(id<AssessmentModelImageInfo> _Nullable)imageInfo fullInstructionsOnly:(BOOL)fullInstructionsOnly __attribute__((swift_name("doCopy(identifier:imageInfo:fullInstructionsOnly:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property BOOL fullInstructionsOnly __attribute__((swift_name("fullInstructionsOnly")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property id<AssessmentModelImageInfo> _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("InstructionStepObject.Companion")))
@interface AssessmentModelInstructionStepObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IntFormatOptions")))
@interface AssessmentModelIntFormatOptions : AssessmentModelNumberFormatOptions<AssessmentModelInt *>
- (instancetype)initWithNumberStyle:(AssessmentModelNumberFormatOptionsStyle *)numberStyle usesGroupingSeparator:(BOOL)usesGroupingSeparator minimumValue:(AssessmentModelInt * _Nullable)minimumValue maximumValue:(AssessmentModelInt * _Nullable)maximumValue stepInterval:(AssessmentModelInt * _Nullable)stepInterval __attribute__((swift_name("init(numberStyle:usesGroupingSeparator:minimumValue:maximumValue:stepInterval:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (AssessmentModelNumberFormatOptionsStyle *)component1 __attribute__((swift_name("component1()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelInt * _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelInt * _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelInt * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelIntFormatOptions *)doCopyNumberStyle:(AssessmentModelNumberFormatOptionsStyle *)numberStyle usesGroupingSeparator:(BOOL)usesGroupingSeparator minimumValue:(AssessmentModelInt * _Nullable)minimumValue maximumValue:(AssessmentModelInt * _Nullable)maximumValue stepInterval:(AssessmentModelInt * _Nullable)stepInterval __attribute__((swift_name("doCopy(numberStyle:usesGroupingSeparator:minimumValue:maximumValue:stepInterval:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t maximumFractionDigits __attribute__((swift_name("maximumFractionDigits")));
@property AssessmentModelInt * _Nullable maximumValue __attribute__((swift_name("maximumValue")));
@property AssessmentModelInt * _Nullable minimumValue __attribute__((swift_name("minimumValue")));
@property (readonly) AssessmentModelNumberFormatOptionsStyle *numberStyle __attribute__((swift_name("numberStyle")));
@property (readonly) AssessmentModelNumberType *numberType __attribute__((swift_name("numberType")));
@property AssessmentModelInt * _Nullable stepInterval __attribute__((swift_name("stepInterval")));
@property (readonly) BOOL usesGroupingSeparator __attribute__((swift_name("usesGroupingSeparator")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IntFormatOptions.Companion")))
@interface AssessmentModelIntFormatOptionsCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IntFormatter")))
@interface AssessmentModelIntFormatter : AssessmentModelNumberFormatter<AssessmentModelInt *>
- (instancetype)initWithFormatOptions:(AssessmentModelNumberFormatOptions<AssessmentModelInt *> *)formatOptions __attribute__((swift_name("init(formatOptions:)"))) __attribute__((objc_designated_initializer));
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValueForValue:(AssessmentModelInt * _Nullable)value __attribute__((swift_name("jsonValueFor(value:)")));
- (NSNumber *)toNSNumberValue:(AssessmentModelInt *)value __attribute__((swift_name("toNSNumber(value:)")));
- (AssessmentModelInt * _Nullable)toTypeValue:(NSNumber * _Nullable)value __attribute__((swift_name("toType(value:)")));
- (AssessmentModelInt * _Nullable)valueForJsonValue:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValue __attribute__((swift_name("valueFor(jsonValue:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IntegerTextInputItemObject")))
@interface AssessmentModelIntegerTextInputItemObject : AssessmentModelInputItemObject <AssessmentModelKeyboardTextInputItem>
- (instancetype)initWithResultIdentifier:(NSString * _Nullable)resultIdentifier textOptions:(AssessmentModelKeyboardOptionsObject *)textOptions formatOptions:(AssessmentModelIntFormatOptions *)formatOptions __attribute__((swift_name("init(resultIdentifier:textOptions:formatOptions:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (id<AssessmentModelTextValidator>)buildTextValidator __attribute__((swift_name("buildTextValidator()")));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelKeyboardOptionsObject *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelIntFormatOptions *)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelIntegerTextInputItemObject *)doCopyResultIdentifier:(NSString * _Nullable)resultIdentifier textOptions:(AssessmentModelKeyboardOptionsObject *)textOptions formatOptions:(AssessmentModelIntFormatOptions *)formatOptions __attribute__((swift_name("doCopy(resultIdentifier:textOptions:formatOptions:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelAnswerType *answerType __attribute__((swift_name("answerType")));
@property AssessmentModelIntFormatOptions *formatOptions __attribute__((swift_name("formatOptions")));
@property (readonly) id<AssessmentModelKeyboardOptions> keyboardOptions __attribute__((swift_name("keyboardOptions")));
@property (readonly) NSString * _Nullable resultIdentifier __attribute__((swift_name("resultIdentifier")));
@property AssessmentModelKeyboardOptionsObject *textOptions __attribute__((swift_name("textOptions")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IntegerTextInputItemObject.Companion")))
@interface AssessmentModelIntegerTextInputItemObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("JsonElementDecoder")))
@interface AssessmentModelJsonElementDecoder : AssessmentModelBase
- (instancetype)initWithJsonString:(NSString *)jsonString __attribute__((swift_name("init(jsonString:)"))) __attribute__((objc_designated_initializer));

/**
 @note This method converts all Kotlin exceptions to errors.
*/
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)decodeObjectAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("decodeObject()")));
@property AssessmentModelKotlinx_serialization_jsonJson *jsonCoder __attribute__((swift_name("jsonCoder")));
@property (readonly) NSString *jsonString __attribute__((swift_name("jsonString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("JsonElementEncoder")))
@interface AssessmentModelJsonElementEncoder : AssessmentModelBase
- (instancetype)initWithJsonElement:(AssessmentModelKotlinx_serialization_jsonJsonElement *)jsonElement __attribute__((swift_name("init(jsonElement:)"))) __attribute__((objc_designated_initializer));

/**
 @note This method converts all Kotlin exceptions to errors.
*/
- (NSString * _Nullable)encodeObjectAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeObject()")));
@property AssessmentModelKotlinx_serialization_jsonJson *jsonCoder __attribute__((swift_name("jsonCoder")));
@property (readonly) AssessmentModelKotlinx_serialization_jsonJsonElement *jsonElement __attribute__((swift_name("jsonElement")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KeyboardOptionsObject")))
@interface AssessmentModelKeyboardOptionsObject : AssessmentModelBase <AssessmentModelKeyboardOptions>
- (instancetype)initWithIsSecureTextEntry:(BOOL)isSecureTextEntry autocapitalizationType:(AssessmentModelAutoCapitalizationType *)autocapitalizationType autocorrectionType:(AssessmentModelAutoCorrectionType *)autocorrectionType spellCheckingType:(AssessmentModelSpellCheckingType *)spellCheckingType keyboardType:(AssessmentModelKeyboardType *)keyboardType __attribute__((swift_name("init(isSecureTextEntry:autocapitalizationType:autocorrectionType:spellCheckingType:keyboardType:)"))) __attribute__((objc_designated_initializer));
- (BOOL)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelAutoCapitalizationType *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelAutoCorrectionType *)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelSpellCheckingType *)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelKeyboardType *)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelKeyboardOptionsObject *)doCopyIsSecureTextEntry:(BOOL)isSecureTextEntry autocapitalizationType:(AssessmentModelAutoCapitalizationType *)autocapitalizationType autocorrectionType:(AssessmentModelAutoCorrectionType *)autocorrectionType spellCheckingType:(AssessmentModelSpellCheckingType *)spellCheckingType keyboardType:(AssessmentModelKeyboardType *)keyboardType __attribute__((swift_name("doCopy(isSecureTextEntry:autocapitalizationType:autocorrectionType:spellCheckingType:keyboardType:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelAutoCapitalizationType *autocapitalizationType __attribute__((swift_name("autocapitalizationType")));
@property (readonly) AssessmentModelAutoCorrectionType *autocorrectionType __attribute__((swift_name("autocorrectionType")));
@property (readonly) BOOL isSecureTextEntry __attribute__((swift_name("isSecureTextEntry")));
@property (readonly) AssessmentModelKeyboardType *keyboardType __attribute__((swift_name("keyboardType")));
@property (readonly) AssessmentModelSpellCheckingType *spellCheckingType __attribute__((swift_name("spellCheckingType")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KeyboardOptionsObject.Companion")))
@interface AssessmentModelKeyboardOptionsObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) AssessmentModelKeyboardOptionsObject *DateTimeEntryOptions __attribute__((swift_name("DateTimeEntryOptions")));
@property (readonly) AssessmentModelKeyboardOptionsObject *DecimalEntryOptions __attribute__((swift_name("DecimalEntryOptions")));
@property (readonly) AssessmentModelKeyboardOptionsObject *NumberEntryOptions __attribute__((swift_name("NumberEntryOptions")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinDecoder")))
@interface AssessmentModelKotlinDecoder : AssessmentModelBase <AssessmentModelResourceInfo>
- (instancetype)initWithBundle:(NSBundle *)bundle __attribute__((swift_name("init(bundle:)"))) __attribute__((objc_designated_initializer));
@property (readonly) NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property id _Nullable decoderBundle __attribute__((swift_name("decoderBundle")));
@property id<AssessmentModelFileLoader> fileLoader __attribute__((swift_name("fileLoader")));
@property AssessmentModelKotlinx_serialization_jsonJson *jsonCoder __attribute__((swift_name("jsonCoder")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Localization")))
@interface AssessmentModelLocalization : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)localization __attribute__((swift_name("init()")));
- (NSString *)localizeStringStringKey:(NSString *)stringKey __attribute__((swift_name("localizeString(stringKey:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MultipleInputQuestionObject")))
@interface AssessmentModelMultipleInputQuestionObject : AssessmentModelQuestionObject <AssessmentModelMultipleInputQuestion>
- (instancetype)initWithIdentifier:(NSString *)identifier inputItems:(NSArray<id<AssessmentModelInputItem>> *)inputItems sequenceSeparator:(NSString * _Nullable)sequenceSeparator skipCheckbox:(id<AssessmentModelSkipCheckboxInputItem> _Nullable)skipCheckbox __attribute__((swift_name("init(identifier:inputItems:sequenceSeparator:skipCheckbox:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSArray<id<AssessmentModelInputItem>> *)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (id<AssessmentModelSkipCheckboxInputItem> _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelMultipleInputQuestionObject *)doCopyIdentifier:(NSString *)identifier inputItems:(NSArray<id<AssessmentModelInputItem>> *)inputItems sequenceSeparator:(NSString * _Nullable)sequenceSeparator skipCheckbox:(id<AssessmentModelSkipCheckboxInputItem> _Nullable)skipCheckbox __attribute__((swift_name("doCopy(identifier:inputItems:sequenceSeparator:skipCheckbox:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) NSArray<id<AssessmentModelInputItem>> *inputItems __attribute__((swift_name("inputItems")));
@property NSString * _Nullable sequenceSeparator __attribute__((swift_name("sequenceSeparator")));
@property id<AssessmentModelSkipCheckboxInputItem> _Nullable skipCheckbox __attribute__((swift_name("skipCheckbox")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MultipleInputQuestionObject.Companion")))
@interface AssessmentModelMultipleInputQuestionObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NavigationButtonActionInfoObject")))
@interface AssessmentModelNavigationButtonActionInfoObject : AssessmentModelSerializableButtonActionInfo <AssessmentModelNavigationButtonActionInfo>
- (instancetype)initWithButtonTitle:(NSString * _Nullable)buttonTitle iconName:(NSString * _Nullable)iconName skipToIdentifier:(NSString *)skipToIdentifier packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("init(buttonTitle:iconName:skipToIdentifier:packageName:bundleIdentifier:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelNavigationButtonActionInfoObject *)doCopyButtonTitle:(NSString * _Nullable)buttonTitle iconName:(NSString * _Nullable)iconName skipToIdentifier:(NSString *)skipToIdentifier packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("doCopy(buttonTitle:iconName:skipToIdentifier:packageName:bundleIdentifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property (readonly) NSString * _Nullable buttonTitle __attribute__((swift_name("buttonTitle")));
@property (readonly) NSString * _Nullable iconName __attribute__((swift_name("iconName")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@property (readonly) NSString *skipToIdentifier __attribute__((swift_name("skipToIdentifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NavigationButtonActionInfoObject.Companion")))
@interface AssessmentModelNavigationButtonActionInfoObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NodeContainerObject.Companion")))
@interface AssessmentModelNodeContainerObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NodeObject.Companion")))
@interface AssessmentModelNodeObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NumberFormatOptionsCompanion")))
@interface AssessmentModelNumberFormatOptionsCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AssessmentModelKotlinArray<id<AssessmentModelKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializerTypeSerial0:(id<AssessmentModelKotlinx_serialization_coreKSerializer>)typeSerial0 __attribute__((swift_name("serializer(typeSerial0:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NumberFormatOptionsStyle")))
@interface AssessmentModelNumberFormatOptionsStyle : AssessmentModelKotlinEnum<AssessmentModelNumberFormatOptionsStyle *> <AssessmentModelStringEnum>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelNumberFormatOptionsStyle *none __attribute__((swift_name("none")));
@property (class, readonly) AssessmentModelNumberFormatOptionsStyle *decimal __attribute__((swift_name("decimal")));
@property (class, readonly) AssessmentModelNumberFormatOptionsStyle *currency __attribute__((swift_name("currency")));
@property (class, readonly) AssessmentModelNumberFormatOptionsStyle *percent __attribute__((swift_name("percent")));
@property (class, readonly) AssessmentModelNumberFormatOptionsStyle *scientific __attribute__((swift_name("scientific")));
@property (class, readonly) AssessmentModelNumberFormatOptionsStyle *spellout __attribute__((swift_name("spellout")));
@property (class, readonly) AssessmentModelNumberFormatOptionsStyle *ordinalnumber __attribute__((swift_name("ordinalnumber")));
@property (readonly) NSString * _Nullable serialName __attribute__((swift_name("serialName")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NumberFormatOptionsStyle.Companion")))
@interface AssessmentModelNumberFormatOptionsStyleCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelNumberFormatOptionsStyle *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelNumberFormatOptionsStyle *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OverviewStepObject")))
@interface AssessmentModelOverviewStepObject : AssessmentModelStepObject <AssessmentModelOverviewStep>
- (instancetype)initWithIdentifier:(NSString *)identifier imageInfo:(id<AssessmentModelImageInfo> _Nullable)imageInfo icons:(NSArray<AssessmentModelIconInfoObject *> * _Nullable)icons permissions:(NSArray<AssessmentModelPermissionInfoObject *> * _Nullable)permissions __attribute__((swift_name("init(identifier:imageInfo:icons:permissions:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (id<AssessmentModelImageInfo> _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSArray<AssessmentModelIconInfoObject *> * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSArray<AssessmentModelPermissionInfoObject *> * _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelOverviewStepObject *)doCopyIdentifier:(NSString *)identifier imageInfo:(id<AssessmentModelImageInfo> _Nullable)imageInfo icons:(NSArray<AssessmentModelIconInfoObject *> * _Nullable)icons permissions:(NSArray<AssessmentModelPermissionInfoObject *> * _Nullable)permissions __attribute__((swift_name("doCopy(identifier:imageInfo:icons:permissions:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSArray<AssessmentModelIconInfoObject *> * _Nullable icons __attribute__((swift_name("icons")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property id<AssessmentModelImageInfo> _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@property id<AssessmentModelButtonActionInfo> _Nullable learnMore __attribute__((swift_name("learnMore")));
@property NSArray<AssessmentModelPermissionInfoObject *> * _Nullable permissions __attribute__((swift_name("permissions")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OverviewStepObject.Companion")))
@interface AssessmentModelOverviewStepObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PermissionInfoObject")))
@interface AssessmentModelPermissionInfoObject : AssessmentModelBase <AssessmentModelPermissionInfo>
- (instancetype)initWithPermissionType:(AssessmentModelPermissionType *)permissionType optional:(BOOL)optional requiresBackground:(BOOL)requiresBackground reason:(NSString * _Nullable)reason restrictedMessage:(NSString * _Nullable)restrictedMessage deniedMessage:(NSString * _Nullable)deniedMessage __attribute__((swift_name("init(permissionType:optional:requiresBackground:reason:restrictedMessage:deniedMessage:)"))) __attribute__((objc_designated_initializer));
- (AssessmentModelPermissionType *)component1 __attribute__((swift_name("component1()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (BOOL)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (NSString * _Nullable)component6 __attribute__((swift_name("component6()")));
- (AssessmentModelPermissionInfoObject *)doCopyPermissionType:(AssessmentModelPermissionType *)permissionType optional:(BOOL)optional requiresBackground:(BOOL)requiresBackground reason:(NSString * _Nullable)reason restrictedMessage:(NSString * _Nullable)restrictedMessage deniedMessage:(NSString * _Nullable)deniedMessage __attribute__((swift_name("doCopy(permissionType:optional:requiresBackground:reason:restrictedMessage:deniedMessage:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable deniedMessage __attribute__((swift_name("deniedMessage")));
@property (readonly) BOOL optional __attribute__((swift_name("optional")));
@property (readonly) AssessmentModelPermissionType *permissionType __attribute__((swift_name("permissionType")));
@property (readonly) NSString * _Nullable reason __attribute__((swift_name("reason")));
@property (readonly) BOOL requiresBackground __attribute__((swift_name("requiresBackground")));
@property (readonly) NSString * _Nullable restrictedMessage __attribute__((swift_name("restrictedMessage")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("PermissionInfoObject.Companion")))
@interface AssessmentModelPermissionInfoObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("QuestionObject.Companion")))
@interface AssessmentModelQuestionObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegExValidator")))
@interface AssessmentModelRegExValidator : AssessmentModelBase <AssessmentModelTextValidator>
- (instancetype)initWithPattern:(NSString *)pattern invalidMessage:(AssessmentModelInvalidMessageObject *)invalidMessage __attribute__((swift_name("init(pattern:invalidMessage:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelInvalidMessageObject *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelRegExValidator *)doCopyPattern:(NSString *)pattern invalidMessage:(AssessmentModelInvalidMessageObject *)invalidMessage __attribute__((swift_name("doCopy(pattern:invalidMessage:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValueForValue:(NSString * _Nullable)value __attribute__((swift_name("jsonValueFor(value:)")));
- (AssessmentModelFormattedValue<NSString *> *)localizedStringForValue:(NSString * _Nullable)value __attribute__((swift_name("localizedStringFor(value:)")));
- (NSString *)description __attribute__((swift_name("description()")));
- (AssessmentModelFormattedValue<NSString *> * _Nullable)valueForText:(NSString *)text __attribute__((swift_name("valueFor(text:)")));
- (NSString * _Nullable)valueForJsonValue:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)jsonValue __attribute__((swift_name("valueFor(jsonValue:)")));
@property (readonly) AssessmentModelInvalidMessageObject *invalidMessage __attribute__((swift_name("invalidMessage")));
@property (readonly) NSString *pattern __attribute__((swift_name("pattern")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("RegExValidator.Companion")))
@interface AssessmentModelRegExValidatorCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ReminderButtonActionInfoObject")))
@interface AssessmentModelReminderButtonActionInfoObject : AssessmentModelSerializableButtonActionInfo <AssessmentModelReminderButtonActionInfo>
- (instancetype)initWithButtonTitle:(NSString * _Nullable)buttonTitle iconName:(NSString * _Nullable)iconName reminderIdentifier:(NSString *)reminderIdentifier reminderPrompt:(NSString * _Nullable)reminderPrompt reminderAlert:(NSString * _Nullable)reminderAlert packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("init(buttonTitle:iconName:reminderIdentifier:reminderPrompt:reminderAlert:packageName:bundleIdentifier:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (NSString * _Nullable)component6 __attribute__((swift_name("component6()")));
- (NSString * _Nullable)component7 __attribute__((swift_name("component7()")));
- (AssessmentModelReminderButtonActionInfoObject *)doCopyButtonTitle:(NSString * _Nullable)buttonTitle iconName:(NSString * _Nullable)iconName reminderIdentifier:(NSString *)reminderIdentifier reminderPrompt:(NSString * _Nullable)reminderPrompt reminderAlert:(NSString * _Nullable)reminderAlert packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("doCopy(buttonTitle:iconName:reminderIdentifier:reminderPrompt:reminderAlert:packageName:bundleIdentifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property (readonly) NSString * _Nullable buttonTitle __attribute__((swift_name("buttonTitle")));
@property (readonly) NSString * _Nullable iconName __attribute__((swift_name("iconName")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@property (readonly) NSString * _Nullable reminderAlert __attribute__((swift_name("reminderAlert")));
@property (readonly) NSString *reminderIdentifier __attribute__((swift_name("reminderIdentifier")));
@property (readonly) NSString * _Nullable reminderPrompt __attribute__((swift_name("reminderPrompt")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ReminderButtonActionInfoObject.Companion")))
@interface AssessmentModelReminderButtonActionInfoObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultEncoder")))
@interface AssessmentModelResultEncoder : AssessmentModelBase
- (instancetype)initWithResult:(id<AssessmentModelResult>)result __attribute__((swift_name("init(result:)"))) __attribute__((objc_designated_initializer));

/**
 @note This method converts all Kotlin exceptions to errors.
*/
- (NSString * _Nullable)encodeObjectAndReturnError:(NSError * _Nullable * _Nullable)error __attribute__((swift_name("encodeObject()")));
@property AssessmentModelKotlinx_serialization_jsonJson *jsonCoder __attribute__((swift_name("jsonCoder")));
@property (readonly) id<AssessmentModelResult> result __attribute__((swift_name("result")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultObject")))
@interface AssessmentModelResultObject : AssessmentModelBase <AssessmentModelResult, AssessmentModelResultNavigationRule>
- (instancetype)initWithIdentifier:(NSString *)identifier startDateString:(NSString *)startDateString endDateString:(NSString * _Nullable)endDateString nextNodeIdentifier:(NSString * _Nullable)nextNodeIdentifier __attribute__((swift_name("init(identifier:startDateString:endDateString:nextNodeIdentifier:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelResultObject *)doCopyIdentifier:(NSString *)identifier startDateString:(NSString *)startDateString endDateString:(NSString * _Nullable)endDateString nextNodeIdentifier:(NSString * _Nullable)nextNodeIdentifier __attribute__((swift_name("doCopy(identifier:startDateString:endDateString:nextNodeIdentifier:)")));
- (id<AssessmentModelResult>)doCopyResultIdentifier:(NSString *)identifier __attribute__((swift_name("doCopyResult(identifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSString * _Nullable endDateString __attribute__((swift_name("endDateString")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property NSString * _Nullable nextNodeIdentifier __attribute__((swift_name("nextNodeIdentifier")));
@property NSString *startDateString __attribute__((swift_name("startDateString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultObject.Companion")))
@interface AssessmentModelResultObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultSummaryStepObject")))
@interface AssessmentModelResultSummaryStepObject : AssessmentModelStepObject <AssessmentModelResultSummaryStep>
- (instancetype)initWithIdentifier:(NSString *)identifier scoringResultPath:(AssessmentModelIdentifierPath * _Nullable)scoringResultPath resultTitle:(NSString * _Nullable)resultTitle imageInfo:(id<AssessmentModelImageInfo> _Nullable)imageInfo __attribute__((swift_name("init(identifier:scoringResultPath:resultTitle:imageInfo:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelIdentifierPath * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (id<AssessmentModelImageInfo> _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelResultSummaryStepObject *)doCopyIdentifier:(NSString *)identifier scoringResultPath:(AssessmentModelIdentifierPath * _Nullable)scoringResultPath resultTitle:(NSString * _Nullable)resultTitle imageInfo:(id<AssessmentModelImageInfo> _Nullable)imageInfo __attribute__((swift_name("doCopy(identifier:scoringResultPath:resultTitle:imageInfo:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property id<AssessmentModelImageInfo> _Nullable imageInfo __attribute__((swift_name("imageInfo")));
@property NSString * _Nullable resultTitle __attribute__((swift_name("resultTitle")));
@property (readonly) AssessmentModelIdentifierPath * _Nullable scoringResultPath __attribute__((swift_name("scoringResultPath")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultSummaryStepObject.Companion")))
@interface AssessmentModelResultSummaryStepObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SectionObject")))
@interface AssessmentModelSectionObject : AssessmentModelNodeContainerObject <AssessmentModelSection, AssessmentModelAsyncActionContainer>
- (instancetype)initWithIdentifier:(NSString *)identifier children:(NSArray<id<AssessmentModelNode>> *)children backgroundActions:(NSArray<id<AssessmentModelAsyncActionConfiguration>> *)backgroundActions __attribute__((swift_name("init(identifier:children:backgroundActions:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSArray<id<AssessmentModelNode>> *)component2 __attribute__((swift_name("component2()")));
- (NSArray<id<AssessmentModelAsyncActionConfiguration>> *)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelSectionObject *)doCopyIdentifier:(NSString *)identifier children:(NSArray<id<AssessmentModelNode>> *)children backgroundActions:(NSArray<id<AssessmentModelAsyncActionConfiguration>> *)backgroundActions __attribute__((swift_name("doCopy(identifier:children:backgroundActions:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
- (AssessmentModelSectionObject *)unpackFileLoader:(id<AssessmentModelFileLoader>)fileLoader resourceInfo:(id<AssessmentModelResourceInfo>)resourceInfo jsonCoder:(AssessmentModelKotlinx_serialization_jsonJson *)jsonCoder __attribute__((swift_name("unpack(fileLoader:resourceInfo:jsonCoder:)")));
@property (readonly) NSArray<id<AssessmentModelAsyncActionConfiguration>> *backgroundActions __attribute__((swift_name("backgroundActions")));
@property (readonly) NSArray<id<AssessmentModelNode>> *children __attribute__((swift_name("children")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SectionObject.Companion")))
@interface AssessmentModelSectionObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SerializableButtonActionInfo.Companion")))
@interface AssessmentModelSerializableButtonActionInfoCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Serialization")))
@interface AssessmentModelSerialization : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)serialization __attribute__((swift_name("init()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Serialization.JsonCoder")))
@interface AssessmentModelSerializationJsonCoder : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)jsonCoder __attribute__((swift_name("init()")));
@property (readonly, getter=default) AssessmentModelKotlinx_serialization_jsonJson *default_ __attribute__((swift_name("default_")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Serialization.SerializersModule")))
@interface AssessmentModelSerializationSerializersModule : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)serializersModule __attribute__((swift_name("init()")));
@property (readonly, getter=default) AssessmentModelKotlinx_serialization_coreSerializersModule *default_ __attribute__((swift_name("default_")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SimpleQuestionObject")))
@interface AssessmentModelSimpleQuestionObject : AssessmentModelQuestionObject <AssessmentModelSimpleQuestion>
- (instancetype)initWithIdentifier:(NSString *)identifier inputItem:(id<AssessmentModelInputItem>)inputItem skipCheckbox:(id<AssessmentModelSkipCheckboxInputItem> _Nullable)skipCheckbox __attribute__((swift_name("init(identifier:inputItem:skipCheckbox:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (id<AssessmentModelInputItem>)component2 __attribute__((swift_name("component2()")));
- (id<AssessmentModelSkipCheckboxInputItem> _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelSimpleQuestionObject *)doCopyIdentifier:(NSString *)identifier inputItem:(id<AssessmentModelInputItem>)inputItem skipCheckbox:(id<AssessmentModelSkipCheckboxInputItem> _Nullable)skipCheckbox __attribute__((swift_name("doCopy(identifier:inputItem:skipCheckbox:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) id<AssessmentModelInputItem> inputItem __attribute__((swift_name("inputItem")));
@property id<AssessmentModelSkipCheckboxInputItem> _Nullable skipCheckbox __attribute__((swift_name("skipCheckbox")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SimpleQuestionObject.Companion")))
@interface AssessmentModelSimpleQuestionObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Size")))
@interface AssessmentModelSize : AssessmentModelBase
- (instancetype)initWithWidth:(double)width height:(double)height __attribute__((swift_name("init(width:height:)"))) __attribute__((objc_designated_initializer));
- (double)component1 __attribute__((swift_name("component1()")));
- (double)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelSize *)doCopyWidth:(double)width height:(double)height __attribute__((swift_name("doCopy(width:height:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double height __attribute__((swift_name("height")));
@property (readonly) double width __attribute__((swift_name("width")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Size.Companion")))
@interface AssessmentModelSizeCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkipCheckboxInputItemObject")))
@interface AssessmentModelSkipCheckboxInputItemObject : AssessmentModelBase <AssessmentModelSkipCheckboxInputItem>
- (instancetype)initWithFieldLabel:(NSString *)fieldLabel value:(AssessmentModelKotlinx_serialization_jsonJsonElement *)value __attribute__((swift_name("init(fieldLabel:value:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelSkipCheckboxInputItemObject *)doCopyFieldLabel:(NSString *)fieldLabel value:(AssessmentModelKotlinx_serialization_jsonJsonElement *)value __attribute__((swift_name("doCopy(fieldLabel:value:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *fieldLabel __attribute__((swift_name("fieldLabel")));
@property (readonly) AssessmentModelKotlinx_serialization_jsonJsonElement *value __attribute__((swift_name("value")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SkipCheckboxInputItemObject.Companion")))
@interface AssessmentModelSkipCheckboxInputItemObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("StepObject.Companion")))
@interface AssessmentModelStepObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("StringChoiceQuestionObject")))
@interface AssessmentModelStringChoiceQuestionObject : AssessmentModelQuestionObject <AssessmentModelChoiceQuestion>
- (instancetype)initWithIdentifier:(NSString *)identifier items:(NSArray<NSString *> *)items singleAnswer:(BOOL)singleAnswer uiHint:(AssessmentModelUIHint *)uiHint __attribute__((swift_name("init(identifier:items:singleAnswer:uiHint:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSArray<NSString *> *)component2 __attribute__((swift_name("component2()")));
- (BOOL)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelUIHint *)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelStringChoiceQuestionObject *)doCopyIdentifier:(NSString *)identifier items:(NSArray<NSString *> *)items singleAnswer:(BOOL)singleAnswer uiHint:(AssessmentModelUIHint *)uiHint __attribute__((swift_name("doCopy(identifier:items:singleAnswer:uiHint:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelBaseType *baseType __attribute__((swift_name("baseType")));
@property (readonly) NSArray<AssessmentModelChoiceOptionObject *> *choices __attribute__((swift_name("choices")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) NSArray<NSString *> *items __attribute__((swift_name("items")));
@property BOOL singleAnswer __attribute__((swift_name("singleAnswer")));
@property AssessmentModelUIHint *uiHint __attribute__((swift_name("uiHint")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("StringChoiceQuestionObject.Companion")))
@interface AssessmentModelStringChoiceQuestionObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("StringTextInputItemObject")))
@interface AssessmentModelStringTextInputItemObject : AssessmentModelInputItemObject <AssessmentModelKeyboardTextInputItem>
- (instancetype)initWithResultIdentifier:(NSString * _Nullable)resultIdentifier textOptions:(AssessmentModelKeyboardOptionsObject *)textOptions regExValidator:(AssessmentModelRegExValidator * _Nullable)regExValidator __attribute__((swift_name("init(resultIdentifier:textOptions:regExValidator:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (id<AssessmentModelTextValidator>)buildTextValidator __attribute__((swift_name("buildTextValidator()")));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelKeyboardOptionsObject *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelRegExValidator * _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelStringTextInputItemObject *)doCopyResultIdentifier:(NSString * _Nullable)resultIdentifier textOptions:(AssessmentModelKeyboardOptionsObject *)textOptions regExValidator:(AssessmentModelRegExValidator * _Nullable)regExValidator __attribute__((swift_name("doCopy(resultIdentifier:textOptions:regExValidator:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelAnswerType *answerType __attribute__((swift_name("answerType")));
@property (readonly) id<AssessmentModelKeyboardOptions> keyboardOptions __attribute__((swift_name("keyboardOptions")));
@property AssessmentModelRegExValidator * _Nullable regExValidator __attribute__((swift_name("regExValidator")));
@property (readonly) NSString * _Nullable resultIdentifier __attribute__((swift_name("resultIdentifier")));
@property AssessmentModelKeyboardOptionsObject *textOptions __attribute__((swift_name("textOptions")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("StringTextInputItemObject.Companion")))
@interface AssessmentModelStringTextInputItemObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimeFormatOptions")))
@interface AssessmentModelTimeFormatOptions : AssessmentModelBase <AssessmentModelDateTimeFormatOptions>
- (instancetype)initWithAllowFuture:(BOOL)allowFuture allowPast:(BOOL)allowPast minimumValue:(NSString * _Nullable)minimumValue maximumValue:(NSString * _Nullable)maximumValue codingFormat:(NSString *)codingFormat __attribute__((swift_name("init(allowFuture:allowPast:minimumValue:maximumValue:codingFormat:)"))) __attribute__((objc_designated_initializer));
- (BOOL)component1 __attribute__((swift_name("component1()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString *)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelTimeFormatOptions *)doCopyAllowFuture:(BOOL)allowFuture allowPast:(BOOL)allowPast minimumValue:(NSString * _Nullable)minimumValue maximumValue:(NSString * _Nullable)maximumValue codingFormat:(NSString *)codingFormat __attribute__((swift_name("doCopy(allowFuture:allowPast:minimumValue:maximumValue:codingFormat:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL allowFuture __attribute__((swift_name("allowFuture")));
@property (readonly) BOOL allowPast __attribute__((swift_name("allowPast")));
@property (readonly) NSString *codingFormat __attribute__((swift_name("codingFormat")));
@property (readonly) NSString * _Nullable maximumValue __attribute__((swift_name("maximumValue")));
@property (readonly) NSString * _Nullable minimumValue __attribute__((swift_name("minimumValue")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimeFormatOptions.Companion")))
@interface AssessmentModelTimeFormatOptionsCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimeInputItemObject")))
@interface AssessmentModelTimeInputItemObject : AssessmentModelInputItemObject <AssessmentModelDateTimeInputItem>
- (instancetype)initWithResultIdentifier:(NSString * _Nullable)resultIdentifier formatOptions:(AssessmentModelTimeFormatOptions *)formatOptions __attribute__((swift_name("init(resultIdentifier:formatOptions:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelTimeFormatOptions *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelTimeInputItemObject *)doCopyResultIdentifier:(NSString * _Nullable)resultIdentifier formatOptions:(AssessmentModelTimeFormatOptions *)formatOptions __attribute__((swift_name("doCopy(resultIdentifier:formatOptions:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property AssessmentModelTimeFormatOptions *formatOptions __attribute__((swift_name("formatOptions")));
@property (readonly) NSString * _Nullable resultIdentifier __attribute__((swift_name("resultIdentifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TimeInputItemObject.Companion")))
@interface AssessmentModelTimeInputItemObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("TransformableNode")))
@protocol AssessmentModelTransformableNode <AssessmentModelContentNode, AssessmentModelAssetInfo>
@required
@end;

__attribute__((swift_name("TransformableAssessment")))
@protocol AssessmentModelTransformableAssessment <AssessmentModelAssessment, AssessmentModelTransformableNode>
@required
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TransformableAssessmentObject")))
@interface AssessmentModelTransformableAssessmentObject : AssessmentModelIconNodeObject <AssessmentModelTransformableAssessment>
- (instancetype)initWithIdentifier:(NSString *)identifier resourceName:(NSString *)resourceName versionString:(NSString * _Nullable)versionString estimatedMinutes:(int32_t)estimatedMinutes schemaIdentifier:(NSString * _Nullable)schemaIdentifier __attribute__((swift_name("init(identifier:resourceName:versionString:estimatedMinutes:schemaIdentifier:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (int32_t)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelTransformableAssessmentObject *)doCopyIdentifier:(NSString *)identifier resourceName:(NSString *)resourceName versionString:(NSString * _Nullable)versionString estimatedMinutes:(int32_t)estimatedMinutes schemaIdentifier:(NSString * _Nullable)schemaIdentifier __attribute__((swift_name("doCopy(identifier:resourceName:versionString:estimatedMinutes:schemaIdentifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t estimatedMinutes __attribute__((swift_name("estimatedMinutes")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) NSString *resourceName __attribute__((swift_name("resourceName")));
@property (readonly) NSString * _Nullable schemaIdentifier __attribute__((swift_name("schemaIdentifier")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TransformableAssessmentObject.Companion")))
@interface AssessmentModelTransformableAssessmentObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TransformableNodeObject")))
@interface AssessmentModelTransformableNodeObject : AssessmentModelIconNodeObject <AssessmentModelTransformableNode>
- (instancetype)initWithIdentifier:(NSString *)identifier resourceName:(NSString *)resourceName versionString:(NSString * _Nullable)versionString __attribute__((swift_name("init(identifier:resourceName:versionString:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (NSString *)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelTransformableNodeObject *)doCopyIdentifier:(NSString *)identifier resourceName:(NSString *)resourceName versionString:(NSString * _Nullable)versionString __attribute__((swift_name("doCopy(identifier:resourceName:versionString:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) NSString *resourceName __attribute__((swift_name("resourceName")));
@property (readonly) NSString * _Nullable versionString __attribute__((swift_name("versionString")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("TransformableNodeObject.Companion")))
@interface AssessmentModelTransformableNodeObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VideoViewButtonActionInfoObject")))
@interface AssessmentModelVideoViewButtonActionInfoObject : AssessmentModelSerializableButtonActionInfo <AssessmentModelVideoViewButtonActionInfo>
- (instancetype)initWithButtonTitle:(NSString * _Nullable)buttonTitle iconName:(NSString * _Nullable)iconName url:(NSString *)url title:(NSString * _Nullable)title packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("init(buttonTitle:iconName:url:title:packageName:bundleIdentifier:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (NSString * _Nullable)component6 __attribute__((swift_name("component6()")));
- (AssessmentModelVideoViewButtonActionInfoObject *)doCopyButtonTitle:(NSString * _Nullable)buttonTitle iconName:(NSString * _Nullable)iconName url:(NSString *)url title:(NSString * _Nullable)title packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("doCopy(buttonTitle:iconName:url:title:packageName:bundleIdentifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property (readonly) NSString * _Nullable buttonTitle __attribute__((swift_name("buttonTitle")));
@property (readonly) NSString * _Nullable iconName __attribute__((swift_name("iconName")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@property (readonly) NSString * _Nullable title __attribute__((swift_name("title")));
@property (readonly) NSString *url __attribute__((swift_name("url")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("VideoViewButtonActionInfoObject.Companion")))
@interface AssessmentModelVideoViewButtonActionInfoObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ViewThemeObject")))
@interface AssessmentModelViewThemeObject : AssessmentModelBase <AssessmentModelViewTheme>
- (instancetype)initWithViewIdentifier:(NSString * _Nullable)viewIdentifier storyboardIdentifier:(NSString * _Nullable)storyboardIdentifier fragmentIdentifier:(NSString * _Nullable)fragmentIdentifier fragmentLayout:(NSString * _Nullable)fragmentLayout bundleIdentifier:(NSString * _Nullable)bundleIdentifier packageName:(NSString * _Nullable)packageName decoderBundle:(id _Nullable)decoderBundle __attribute__((swift_name("init(viewIdentifier:storyboardIdentifier:fragmentIdentifier:fragmentLayout:bundleIdentifier:packageName:decoderBundle:)"))) __attribute__((objc_designated_initializer));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString * _Nullable)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSString * _Nullable)component5 __attribute__((swift_name("component5()")));
- (NSString * _Nullable)component6 __attribute__((swift_name("component6()")));
- (id _Nullable)component7 __attribute__((swift_name("component7()")));
- (AssessmentModelViewThemeObject *)doCopyViewIdentifier:(NSString * _Nullable)viewIdentifier storyboardIdentifier:(NSString * _Nullable)storyboardIdentifier fragmentIdentifier:(NSString * _Nullable)fragmentIdentifier fragmentLayout:(NSString * _Nullable)fragmentLayout bundleIdentifier:(NSString * _Nullable)bundleIdentifier packageName:(NSString * _Nullable)packageName decoderBundle:(id _Nullable)decoderBundle __attribute__((swift_name("doCopy(viewIdentifier:storyboardIdentifier:fragmentIdentifier:fragmentLayout:bundleIdentifier:packageName:decoderBundle:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property id _Nullable decoderBundle __attribute__((swift_name("decoderBundle")));
@property (readonly) NSString * _Nullable fragmentIdentifier __attribute__((swift_name("fragmentIdentifier")));
@property (readonly) NSString * _Nullable fragmentLayout __attribute__((swift_name("fragmentLayout")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@property (readonly) NSString * _Nullable storyboardIdentifier __attribute__((swift_name("storyboardIdentifier")));
@property (readonly) NSString * _Nullable viewIdentifier __attribute__((swift_name("viewIdentifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ViewThemeObject.Companion")))
@interface AssessmentModelViewThemeObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("WebViewButtonActionInfoObject")))
@interface AssessmentModelWebViewButtonActionInfoObject : AssessmentModelSerializableButtonActionInfo <AssessmentModelWebViewButtonActionInfo>
- (instancetype)initWithButtonTitle:(NSString * _Nullable)buttonTitle iconName:(NSString * _Nullable)iconName url:(NSString *)url title:(NSString * _Nullable)title usesBackButton:(AssessmentModelBoolean * _Nullable)usesBackButton closeButtonTitle:(NSString * _Nullable)closeButtonTitle packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("init(buttonTitle:iconName:url:title:usesBackButton:closeButtonTitle:packageName:bundleIdentifier:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSString *)component3 __attribute__((swift_name("component3()")));
- (NSString * _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelBoolean * _Nullable)component5 __attribute__((swift_name("component5()")));
- (NSString * _Nullable)component6 __attribute__((swift_name("component6()")));
- (NSString * _Nullable)component7 __attribute__((swift_name("component7()")));
- (NSString * _Nullable)component8 __attribute__((swift_name("component8()")));
- (AssessmentModelWebViewButtonActionInfoObject *)doCopyButtonTitle:(NSString * _Nullable)buttonTitle iconName:(NSString * _Nullable)iconName url:(NSString *)url title:(NSString * _Nullable)title usesBackButton:(AssessmentModelBoolean * _Nullable)usesBackButton closeButtonTitle:(NSString * _Nullable)closeButtonTitle packageName:(NSString * _Nullable)packageName bundleIdentifier:(NSString * _Nullable)bundleIdentifier __attribute__((swift_name("doCopy(buttonTitle:iconName:url:title:usesBackButton:closeButtonTitle:packageName:bundleIdentifier:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelButtonStyle *backButtonStyle __attribute__((swift_name("backButtonStyle")));
@property NSString * _Nullable bundleIdentifier __attribute__((swift_name("bundleIdentifier")));
@property (readonly) NSString * _Nullable buttonTitle __attribute__((swift_name("buttonTitle")));
@property (readonly) NSString * _Nullable closeButtonTitle __attribute__((swift_name("closeButtonTitle")));
@property (readonly) NSString * _Nullable iconName __attribute__((swift_name("iconName")));
@property NSString * _Nullable packageName __attribute__((swift_name("packageName")));
@property (readonly) NSString * _Nullable title __attribute__((swift_name("title")));
@property (readonly) NSString *url __attribute__((swift_name("url")));
@property (readonly) AssessmentModelBoolean * _Nullable usesBackButton __attribute__((swift_name("usesBackButton")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("WebViewButtonActionInfoObject.Companion")))
@interface AssessmentModelWebViewButtonActionInfoObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("YearFormatOptions")))
@interface AssessmentModelYearFormatOptions : AssessmentModelNumberFormatOptions<AssessmentModelInt *>
- (instancetype)initWithAllowFuture:(BOOL)allowFuture allowPast:(BOOL)allowPast minimumYear:(AssessmentModelInt * _Nullable)minimumYear maximumYear:(AssessmentModelInt * _Nullable)maximumYear __attribute__((swift_name("init(allowFuture:allowPast:minimumYear:maximumYear:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (BOOL)component1 __attribute__((swift_name("component1()")));
- (BOOL)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelInt * _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelInt * _Nullable)component4 __attribute__((swift_name("component4()")));
- (AssessmentModelYearFormatOptions *)doCopyAllowFuture:(BOOL)allowFuture allowPast:(BOOL)allowPast minimumYear:(AssessmentModelInt * _Nullable)minimumYear maximumYear:(AssessmentModelInt * _Nullable)maximumYear __attribute__((swift_name("doCopy(allowFuture:allowPast:minimumYear:maximumYear:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property BOOL allowFuture __attribute__((swift_name("allowFuture")));
@property BOOL allowPast __attribute__((swift_name("allowPast")));
@property (readonly) int32_t maximumFractionDigits __attribute__((swift_name("maximumFractionDigits")));
@property (readonly) AssessmentModelInt * _Nullable maximumValue __attribute__((swift_name("maximumValue")));
@property AssessmentModelInt * _Nullable maximumYear __attribute__((swift_name("maximumYear")));
@property (readonly) AssessmentModelInt * _Nullable minimumValue __attribute__((swift_name("minimumValue")));
@property AssessmentModelInt * _Nullable minimumYear __attribute__((swift_name("minimumYear")));
@property (readonly) AssessmentModelNumberFormatOptionsStyle *numberStyle __attribute__((swift_name("numberStyle")));
@property (readonly) AssessmentModelNumberType *numberType __attribute__((swift_name("numberType")));
@property (readonly) AssessmentModelInt * _Nullable stepInterval __attribute__((swift_name("stepInterval")));
@property (readonly) BOOL usesGroupingSeparator __attribute__((swift_name("usesGroupingSeparator")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("YearFormatOptions.Companion")))
@interface AssessmentModelYearFormatOptionsCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("YearTextInputItemObject")))
@interface AssessmentModelYearTextInputItemObject : AssessmentModelInputItemObject <AssessmentModelKeyboardTextInputItem>
- (instancetype)initWithResultIdentifier:(NSString * _Nullable)resultIdentifier formatOptions:(AssessmentModelYearFormatOptions *)formatOptions __attribute__((swift_name("init(resultIdentifier:formatOptions:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
- (id<AssessmentModelTextValidator>)buildTextValidator __attribute__((swift_name("buildTextValidator()")));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelYearFormatOptions *)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelYearTextInputItemObject *)doCopyResultIdentifier:(NSString * _Nullable)resultIdentifier formatOptions:(AssessmentModelYearFormatOptions *)formatOptions __attribute__((swift_name("doCopy(resultIdentifier:formatOptions:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelAnswerType *answerType __attribute__((swift_name("answerType")));
@property AssessmentModelYearFormatOptions *formatOptions __attribute__((swift_name("formatOptions")));
@property (readonly) id<AssessmentModelKeyboardOptions> keyboardOptions __attribute__((swift_name("keyboardOptions")));
@property (readonly) NSString * _Nullable resultIdentifier __attribute__((swift_name("resultIdentifier")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("YearTextInputItemObject.Companion")))
@interface AssessmentModelYearTextInputItemObjectCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AsyncActionNavigation")))
@interface AssessmentModelAsyncActionNavigation : AssessmentModelBase
- (instancetype)initWithSectionIdentifier:(NSString * _Nullable)sectionIdentifier startAsyncActions:(NSSet<id<AssessmentModelAsyncActionConfiguration>> * _Nullable)startAsyncActions stopAsyncActions:(NSSet<id<AssessmentModelAsyncActionConfiguration>> * _Nullable)stopAsyncActions __attribute__((swift_name("init(sectionIdentifier:startAsyncActions:stopAsyncActions:)"))) __attribute__((objc_designated_initializer));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()")));
- (NSSet<id<AssessmentModelAsyncActionConfiguration>> * _Nullable)component2 __attribute__((swift_name("component2()")));
- (NSSet<id<AssessmentModelAsyncActionConfiguration>> * _Nullable)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelAsyncActionNavigation *)doCopySectionIdentifier:(NSString * _Nullable)sectionIdentifier startAsyncActions:(NSSet<id<AssessmentModelAsyncActionConfiguration>> * _Nullable)startAsyncActions stopAsyncActions:(NSSet<id<AssessmentModelAsyncActionConfiguration>> * _Nullable)stopAsyncActions __attribute__((swift_name("doCopy(sectionIdentifier:startAsyncActions:stopAsyncActions:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (BOOL)isEmpty __attribute__((swift_name("isEmpty()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable sectionIdentifier __attribute__((swift_name("sectionIdentifier")));
@property (readonly) NSSet<id<AssessmentModelAsyncActionConfiguration>> * _Nullable startAsyncActions __attribute__((swift_name("startAsyncActions")));
@property (readonly) NSSet<id<AssessmentModelAsyncActionConfiguration>> * _Nullable stopAsyncActions __attribute__((swift_name("stopAsyncActions")));
@end;

__attribute__((swift_name("BranchNodeState")))
@protocol AssessmentModelBranchNodeState <AssessmentModelNodeState>
@required
- (void)exitEarlyAsyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("exitEarly(asyncActionNavigations:)")));
- (void)moveToNextNodeDirection:(AssessmentModelNavigationPointDirection *)direction requestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("moveToNextNode(direction:requestedPermissions:asyncActionNavigations:)")));
@property (readonly) id<AssessmentModelNodeState> _Nullable currentChild __attribute__((swift_name("currentChild")));
@property id<AssessmentModelRootNodeController> _Nullable rootNodeController __attribute__((swift_name("rootNodeController")));
@end;

__attribute__((swift_name("BranchNodeStateImpl")))
@interface AssessmentModelBranchNodeStateImpl : AssessmentModelBase <AssessmentModelBranchNodeState>
- (instancetype)initWithNode:(id<AssessmentModelBranchNode>)node parent:(id<AssessmentModelBranchNodeState> _Nullable)parent __attribute__((swift_name("init(node:parent:)"))) __attribute__((objc_designated_initializer));
- (void)appendChildResultIfNeeded __attribute__((swift_name("appendChildResultIfNeeded()")));
- (void)exitEarlyAsyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("exitEarly(asyncActionNavigations:)")));
- (void)finishNavigationPoint:(AssessmentModelNavigationPoint *)navigationPoint __attribute__((swift_name("finish(navigationPoint:)")));
- (id<AssessmentModelBranchNodeState> _Nullable)getBranchNodeStateNavigationPoint:(AssessmentModelNavigationPoint *)navigationPoint __attribute__((swift_name("getBranchNodeState(navigationPoint:)")));
- (id<AssessmentModelNodeState> _Nullable)getLeafNodeStateNavigationPoint:(AssessmentModelNavigationPoint *)navigationPoint __attribute__((swift_name("getLeafNodeState(navigationPoint:)")));
- (AssessmentModelNavigationPoint * _Nullable)getNextNodeInDirection:(AssessmentModelNavigationPointDirection *)inDirection __attribute__((swift_name("getNextNode(inDirection:)")));
- (void)goBackwardRequestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("goBackward(requestedPermissions:asyncActionNavigations:)")));
- (void)goForwardRequestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("goForward(requestedPermissions:asyncActionNavigations:)")));
- (void)moveToNavigationPoint:(AssessmentModelNavigationPoint *)navigationPoint __attribute__((swift_name("moveTo(navigationPoint:)")));
- (void)moveToNextNodeDirection:(AssessmentModelNavigationPointDirection *)direction requestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("moveToNextNode(direction:requestedPermissions:asyncActionNavigations:)")));
- (void)unionNavigationSetsNavigationPoint:(AssessmentModelNavigationPoint *)navigationPoint requestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("unionNavigationSets(navigationPoint:requestedPermissions:asyncActionNavigations:)")));
@property id<AssessmentModelNodeState> _Nullable currentChild __attribute__((swift_name("currentChild")));
@property (readonly) id<AssessmentModelBranchNodeResult> currentResult __attribute__((swift_name("currentResult")));
@property (readonly) id<AssessmentModelBranchNode> node __attribute__((swift_name("node")));
@property (readonly) id<AssessmentModelBranchNodeState> _Nullable parent __attribute__((swift_name("parent")));
@property id<AssessmentModelRootNodeController> _Nullable rootNodeController __attribute__((swift_name("rootNodeController")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FinishedReason")))
@interface AssessmentModelFinishedReason : AssessmentModelKotlinEnum<AssessmentModelFinishedReason *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelFinishedReason *complete __attribute__((swift_name("complete")));
@property (class, readonly) AssessmentModelFinishedReason *error __attribute__((swift_name("error")));
@property (class, readonly) AssessmentModelFinishedReason *earlyexit __attribute__((swift_name("earlyexit")));
@property (class, readonly) AssessmentModelFinishedReason *discarded __attribute__((swift_name("discarded")));
@property (class, readonly) AssessmentModelFinishedReason *saveprogress __attribute__((swift_name("saveprogress")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IdentifierPath")))
@interface AssessmentModelIdentifierPath : AssessmentModelBase
- (instancetype)initWithPath:(NSString *)path __attribute__((swift_name("init(path:)"))) __attribute__((objc_designated_initializer));
- (NSString *)component1 __attribute__((swift_name("component1()")));
- (AssessmentModelIdentifierPath *)doCopyPath:(NSString *)path __attribute__((swift_name("doCopy(path:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelIdentifierPath * _Nullable child __attribute__((swift_name("child")));
@property (readonly) NSString *identifier __attribute__((swift_name("identifier")));
@property (readonly) NSString *path __attribute__((swift_name("path")));
@property (readonly) NSArray<NSString *> *pathParts __attribute__((swift_name("pathParts")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IdentifierPath.Companion")))
@interface AssessmentModelIdentifierPathCompanion : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreKSerializer>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (AssessmentModelIdentifierPath *)deserializeDecoder:(id<AssessmentModelKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
- (void)serializeEncoder:(id<AssessmentModelKotlinx_serialization_coreEncoder>)encoder value:(AssessmentModelIdentifierPath *)value __attribute__((swift_name("serialize(encoder:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) id<AssessmentModelKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LeafNodeStateImpl")))
@interface AssessmentModelLeafNodeStateImpl : AssessmentModelBase <AssessmentModelLeafNodeState>
- (instancetype)initWithNode:(id<AssessmentModelNode>)node parent:(id<AssessmentModelBranchNodeState>)parent __attribute__((swift_name("init(node:parent:)"))) __attribute__((objc_designated_initializer));
@property (readonly) id<AssessmentModelResult> currentResult __attribute__((swift_name("currentResult")));
@property (readonly) id<AssessmentModelNode> node __attribute__((swift_name("node")));
@property (readonly) id<AssessmentModelBranchNodeState> parent __attribute__((swift_name("parent")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NavigationPoint")))
@interface AssessmentModelNavigationPoint : AssessmentModelBase
- (instancetype)initWithNode:(id<AssessmentModelNode> _Nullable)node branchResult:(id<AssessmentModelBranchNodeResult>)branchResult direction:(AssessmentModelNavigationPointDirection *)direction requestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("init(node:branchResult:direction:requestedPermissions:asyncActionNavigations:)"))) __attribute__((objc_designated_initializer));
- (id<AssessmentModelNode> _Nullable)component1 __attribute__((swift_name("component1()")));
- (id<AssessmentModelBranchNodeResult>)component2 __attribute__((swift_name("component2()")));
- (AssessmentModelNavigationPointDirection *)component3 __attribute__((swift_name("component3()")));
- (NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)component4 __attribute__((swift_name("component4()")));
- (NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)component5 __attribute__((swift_name("component5()")));
- (AssessmentModelNavigationPoint *)doCopyNode:(id<AssessmentModelNode> _Nullable)node branchResult:(id<AssessmentModelBranchNodeResult>)branchResult direction:(AssessmentModelNavigationPointDirection *)direction requestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("doCopy(node:branchResult:direction:requestedPermissions:asyncActionNavigations:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable asyncActionNavigations __attribute__((swift_name("asyncActionNavigations")));
@property (readonly) id<AssessmentModelBranchNodeResult> branchResult __attribute__((swift_name("branchResult")));
@property (readonly) AssessmentModelNavigationPointDirection *direction __attribute__((swift_name("direction")));
@property (readonly) id<AssessmentModelNode> _Nullable node __attribute__((swift_name("node")));
@property NSSet<id<AssessmentModelPermissionInfo>> * _Nullable requestedPermissions __attribute__((swift_name("requestedPermissions")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NavigationPoint.Direction")))
@interface AssessmentModelNavigationPointDirection : AssessmentModelKotlinEnum<AssessmentModelNavigationPointDirection *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AssessmentModelNavigationPointDirection *forward __attribute__((swift_name("forward")));
@property (class, readonly) AssessmentModelNavigationPointDirection *backward __attribute__((swift_name("backward")));
@property (class, readonly) AssessmentModelNavigationPointDirection *exit __attribute__((swift_name("exit")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NavigationPoint.DirectionCompanion")))
@interface AssessmentModelNavigationPointDirectionCompanion : AssessmentModelBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end;

__attribute__((swift_name("Navigator")))
@protocol AssessmentModelNavigator
@required
- (BOOL)allowBackNavigationCurrentNode:(id<AssessmentModelNode>)currentNode branchResult:(id<AssessmentModelBranchNodeResult>)branchResult __attribute__((swift_name("allowBackNavigation(currentNode:branchResult:)")));
- (BOOL)hasNodeAfterCurrentNode:(id<AssessmentModelNode>)currentNode branchResult:(id<AssessmentModelBranchNodeResult>)branchResult __attribute__((swift_name("hasNodeAfter(currentNode:branchResult:)")));
- (id<AssessmentModelNode> _Nullable)nodeIdentifier:(NSString *)identifier __attribute__((swift_name("node(identifier:)")));
- (AssessmentModelNavigationPoint *)nodeAfterCurrentNode:(id<AssessmentModelNode> _Nullable)currentNode branchResult:(id<AssessmentModelBranchNodeResult>)branchResult __attribute__((swift_name("nodeAfter(currentNode:branchResult:)")));
- (AssessmentModelNavigationPoint *)nodeBeforeCurrentNode:(id<AssessmentModelNode> _Nullable)currentNode branchResult:(id<AssessmentModelBranchNodeResult>)branchResult __attribute__((swift_name("nodeBefore(currentNode:branchResult:)")));
- (AssessmentModelProgress * _Nullable)progressCurrentNode:(id<AssessmentModelNode>)currentNode branchResult:(id<AssessmentModelBranchNodeResult>)branchResult __attribute__((swift_name("progress(currentNode:branchResult:)")));
@end;

__attribute__((swift_name("NodeNavigator")))
@interface AssessmentModelNodeNavigator : AssessmentModelBase <AssessmentModelNavigator>
- (instancetype)initWithNode:(id<AssessmentModelNodeContainer>)node __attribute__((swift_name("init(node:)"))) __attribute__((objc_designated_initializer));
- (BOOL)allowBackNavigationCurrentNode:(id<AssessmentModelNode>)currentNode branchResult:(id<AssessmentModelBranchNodeResult>)branchResult __attribute__((swift_name("allowBackNavigation(currentNode:branchResult:)")));
- (NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigationsPreviousNode:(id<AssessmentModelNode> _Nullable)previousNode nextNode:(id<AssessmentModelNode> _Nullable)nextNode parentResult:(id<AssessmentModelBranchNodeResult>)parentResult __attribute__((swift_name("asyncActionNavigations(previousNode:nextNode:parentResult:)")));
- (BOOL)hasNodeAfterCurrentNode:(id<AssessmentModelNode>)currentNode branchResult:(id<AssessmentModelBranchNodeResult>)branchResult __attribute__((swift_name("hasNodeAfter(currentNode:branchResult:)")));
- (id<AssessmentModelNavigationRule> _Nullable)navigationRuleForNode:(id<AssessmentModelNode>)node parentResult:(id<AssessmentModelBranchNodeResult>)parentResult __attribute__((swift_name("navigationRuleFor(node:parentResult:)")));
- (id<AssessmentModelNode> _Nullable)nodeIdentifier:(NSString *)identifier __attribute__((swift_name("node(identifier:)")));
- (AssessmentModelNavigationPoint *)nodeAfterCurrentNode:(id<AssessmentModelNode> _Nullable)currentNode branchResult:(id<AssessmentModelBranchNodeResult>)branchResult __attribute__((swift_name("nodeAfter(currentNode:branchResult:)")));
- (AssessmentModelNavigationPoint *)nodeBeforeCurrentNode:(id<AssessmentModelNode> _Nullable)currentNode branchResult:(id<AssessmentModelBranchNodeResult>)branchResult __attribute__((swift_name("nodeBefore(currentNode:branchResult:)")));
- (AssessmentModelProgress * _Nullable)progressCurrentNode:(id<AssessmentModelNode>)currentNode branchResult:(id<AssessmentModelBranchNodeResult>)branchResult __attribute__((swift_name("progress(currentNode:branchResult:)")));
@property (readonly) id<AssessmentModelAsyncActionContainer> _Nullable asyncActionContainer __attribute__((swift_name("asyncActionContainer")));
@property (readonly) id<AssessmentModelNodeContainer> node __attribute__((swift_name("node")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Progress")))
@interface AssessmentModelProgress : AssessmentModelBase
- (instancetype)initWithCurrent:(int32_t)current total:(int32_t)total isEstimated:(BOOL)isEstimated __attribute__((swift_name("init(current:total:isEstimated:)"))) __attribute__((objc_designated_initializer));
- (int32_t)component1 __attribute__((swift_name("component1()")));
- (int32_t)component2 __attribute__((swift_name("component2()")));
- (BOOL)component3 __attribute__((swift_name("component3()")));
- (AssessmentModelProgress *)doCopyCurrent:(int32_t)current total:(int32_t)total isEstimated:(BOOL)isEstimated __attribute__((swift_name("doCopy(current:total:isEstimated:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t current __attribute__((swift_name("current")));
@property (readonly) BOOL isEstimated __attribute__((swift_name("isEstimated")));
@property (readonly) int32_t total __attribute__((swift_name("total")));
@end;

__attribute__((swift_name("RootNodeController")))
@protocol AssessmentModelRootNodeController
@required
- (BOOL)canHandleNode:(id<AssessmentModelNode>)node __attribute__((swift_name("canHandle(node:)")));
- (id<AssessmentModelNodeState> _Nullable)customNodeStateForNode:(id<AssessmentModelNode>)node parent:(id<AssessmentModelBranchNodeState>)parent __attribute__((swift_name("customNodeStateFor(node:parent:)")));
- (void)handleFinishedReason:(AssessmentModelFinishedReason *)reason nodeState:(id<AssessmentModelNodeState>)nodeState error:(AssessmentModelKotlinError * _Nullable)error __attribute__((swift_name("handleFinished(reason:nodeState:error:)")));
- (void)handleGoBackNodeState:(id<AssessmentModelNodeState>)nodeState requestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("handleGoBack(nodeState:requestedPermissions:asyncActionNavigations:)")));
- (void)handleGoForwardNodeState:(id<AssessmentModelNodeState>)nodeState requestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("handleGoForward(nodeState:requestedPermissions:asyncActionNavigations:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface AssessmentModelKotlinArray<T> : AssessmentModelBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(AssessmentModelInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<AssessmentModelKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

@interface AssessmentModelKotlinArray (Extensions)
- (id<AssessmentModelStringEnum> _Nullable)matchingName:(NSString *)name __attribute__((swift_name("matching(name:)")));
@end;

@interface AssessmentModelDateTimePart (Extensions)
- (uint64_t)calendarUnit __attribute__((swift_name("calendarUnit()")));
@end;

__attribute__((swift_name("Kotlinx_serialization_jsonJsonElement")))
@interface AssessmentModelKotlinx_serialization_jsonJsonElement : AssessmentModelBase
@end;

@interface AssessmentModelKotlinx_serialization_jsonJsonElement (Extensions)
- (BOOL)compareToValue:(AssessmentModelKotlinx_serialization_jsonJsonElement * _Nullable)value operator:(AssessmentModelSurveyRuleOperator *)operator_ accuracy:(double)accuracy __attribute__((swift_name("compareTo(value:operator:accuracy:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AssessmentKt")))
@interface AssessmentModelAssessmentKt : AssessmentModelBase
+ (NSArray<NSString *> *)allNodeIdentifiers:(id<AssessmentModelNodeContainer>)receiver __attribute__((swift_name("allNodeIdentifiers(_:)")));
+ (NSString *)resultId:(id<AssessmentModelResultMapElement>)receiver __attribute__((swift_name("resultId(_:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultKt")))
@interface AssessmentModelResultKt : AssessmentModelBase
+ (NSMutableArray<id<AssessmentModelResult>> *)doCopyResults:(NSMutableArray<id<AssessmentModelResult>> *)receiver __attribute__((swift_name("doCopyResults(_:)")));
+ (AssessmentModelMutableSet<id<AssessmentModelResult>> *)doCopyResults_:(AssessmentModelMutableSet<id<AssessmentModelResult>> *)receiver __attribute__((swift_name("doCopyResults(__:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("StringEnumKt")))
@interface AssessmentModelStringEnumKt : AssessmentModelBase
+ (NSString *)matchingName:(id<AssessmentModelStringEnum>)receiver __attribute__((swift_name("matchingName(_:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AnswerTypeKt")))
@interface AssessmentModelAnswerTypeKt : AssessmentModelBase
@property (class, readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *answerTypeSerializersModule __attribute__((swift_name("answerTypeSerializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DateKt")))
@interface AssessmentModelDateKt : AssessmentModelBase
+ (NSDateFormatter *)serializableDateFormatter __attribute__((swift_name("serializableDateFormatter()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResourceInfoKt")))
@interface AssessmentModelResourceInfoKt : AssessmentModelBase
+ (void)doCopyResourceInfo:(id<AssessmentModelAssetResourceInfo>)receiver fromResourceInfo:(id<AssessmentModelResourceInfo>)fromResourceInfo __attribute__((swift_name("doCopyResourceInfo(_:fromResourceInfo:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("AsyncActionConfigurationsKt")))
@interface AssessmentModelAsyncActionConfigurationsKt : AssessmentModelBase
@property (class, readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *asyncActionSerializersModule __attribute__((swift_name("asyncActionSerializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ButtonsKt")))
@interface AssessmentModelButtonsKt : AssessmentModelBase
@property (class, readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *buttonSerializersModule __attribute__((swift_name("buttonSerializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FileAssessmentProviderKt")))
@interface AssessmentModelFileAssessmentProviderKt : AssessmentModelBase
@property (class, readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *fileProviderSerializersModule __attribute__((swift_name("fileProviderSerializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ImageKt")))
@interface AssessmentModelImageKt : AssessmentModelBase
@property (class, readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *imageSerializersModule __attribute__((swift_name("imageSerializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("InputItemsKt")))
@interface AssessmentModelInputItemsKt : AssessmentModelBase
@property (class, readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *inputItemSerializersModule __attribute__((swift_name("inputItemSerializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NodesKt")))
@interface AssessmentModelNodesKt : AssessmentModelBase
@property (class, readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *nodeSerializersModule __attribute__((swift_name("nodeSerializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ResultsKt")))
@interface AssessmentModelResultsKt : AssessmentModelBase
@property (class, readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *resultSerializersModule __attribute__((swift_name("resultSerializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FileLoaderIOSKt")))
@interface AssessmentModelFileLoaderIOSKt : AssessmentModelBase
+ (NSBundle *)bundle:(id<AssessmentModelResourceInfo>)receiver __attribute__((swift_name("bundle(_:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NodeNavigatorKt")))
@interface AssessmentModelNodeNavigatorKt : AssessmentModelBase
+ (NSSet<id<AssessmentModelAsyncActionConfiguration>> * _Nullable)backgroundActionsToStart:(id<AssessmentModelAsyncActionContainer>)receiver previousNode:(id<AssessmentModelNode> _Nullable)previousNode nextNode:(id<AssessmentModelNode> _Nullable)nextNode __attribute__((swift_name("backgroundActionsToStart(_:previousNode:nextNode:)")));
+ (NSSet<id<AssessmentModelAsyncActionConfiguration>> * _Nullable)backgroundActionsToStop:(id<AssessmentModelAsyncActionContainer>)receiver previousNode:(id<AssessmentModelNode> _Nullable)previousNode nextNode:(id<AssessmentModelNode> _Nullable)nextNode __attribute__((swift_name("backgroundActionsToStop(_:previousNode:nextNode:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NodeStateKt")))
@interface AssessmentModelNodeStateKt : AssessmentModelBase
+ (void)goIn:(id<AssessmentModelNodeState>)receiver direction:(AssessmentModelNavigationPointDirection *)direction requestedPermissions:(NSSet<id<AssessmentModelPermissionInfo>> * _Nullable)requestedPermissions asyncActionNavigations:(NSSet<AssessmentModelAsyncActionNavigation *> * _Nullable)asyncActionNavigations __attribute__((swift_name("goIn(_:direction:requestedPermissions:asyncActionNavigations:)")));
+ (id<AssessmentModelBranchNodeState>)lowestBranch:(id<AssessmentModelBranchNodeState>)receiver __attribute__((swift_name("lowestBranch(_:)")));
+ (id<AssessmentModelResult> _Nullable)previousResult:(id<AssessmentModelNodeState>)receiver __attribute__((swift_name("previousResult(_:)")));
+ (id<AssessmentModelNodeState>)root:(id<AssessmentModelNodeState>)receiver __attribute__((swift_name("root(_:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NavigatorKt")))
@interface AssessmentModelNavigatorKt : AssessmentModelBase
+ (NSSet<AssessmentModelAsyncActionNavigation *> *)union:(NSSet<AssessmentModelAsyncActionNavigation *> *)receiver values:(NSSet<AssessmentModelAsyncActionNavigation *> *)values __attribute__((swift_name("union(_:values:)")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerialFormat")))
@protocol AssessmentModelKotlinx_serialization_coreSerialFormat
@required
@property (readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreStringFormat")))
@protocol AssessmentModelKotlinx_serialization_coreStringFormat <AssessmentModelKotlinx_serialization_coreSerialFormat>
@required
- (id _Nullable)decodeFromStringDeserializer:(id<AssessmentModelKotlinx_serialization_coreDeserializationStrategy>)deserializer string:(NSString *)string __attribute__((swift_name("decodeFromString(deserializer:string:)")));
- (NSString *)encodeToStringSerializer:(id<AssessmentModelKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeToString(serializer:value:)")));
@end;

__attribute__((swift_name("Kotlinx_serialization_jsonJson")))
@interface AssessmentModelKotlinx_serialization_jsonJson : AssessmentModelBase <AssessmentModelKotlinx_serialization_coreStringFormat>
- (id _Nullable)decodeFromJsonElementDeserializer:(id<AssessmentModelKotlinx_serialization_coreDeserializationStrategy>)deserializer element:(AssessmentModelKotlinx_serialization_jsonJsonElement *)element __attribute__((swift_name("decodeFromJsonElement(deserializer:element:)")));
- (id _Nullable)decodeFromStringDeserializer:(id<AssessmentModelKotlinx_serialization_coreDeserializationStrategy>)deserializer string:(NSString *)string __attribute__((swift_name("decodeFromString(deserializer:string:)")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement *)encodeToJsonElementSerializer:(id<AssessmentModelKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeToJsonElement(serializer:value:)")));
- (NSString *)encodeToStringSerializer:(id<AssessmentModelKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeToString(serializer:value:)")));
- (AssessmentModelKotlinx_serialization_jsonJsonElement *)parseToJsonElementString:(NSString *)string __attribute__((swift_name("parseToJsonElement(string:)")));
@property (readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreEncoder")))
@protocol AssessmentModelKotlinx_serialization_coreEncoder
@required
- (id<AssessmentModelKotlinx_serialization_coreCompositeEncoder>)beginCollectionDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize __attribute__((swift_name("beginCollection(descriptor:collectionSize:)")));
- (id<AssessmentModelKotlinx_serialization_coreCompositeEncoder>)beginStructureDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (void)encodeIntValue:(int32_t)value __attribute__((swift_name("encodeInt(value:)")));
- (void)encodeLongValue:(int64_t)value __attribute__((swift_name("encodeLong(value:)")));
- (void)encodeNotNullMark __attribute__((swift_name("encodeNotNullMark()")));
- (void)encodeNull __attribute__((swift_name("encodeNull()")));
- (void)encodeNullableSerializableValueSerializer:(id<AssessmentModelKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<AssessmentModelKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
@property (readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerialDescriptor")))
@protocol AssessmentModelKotlinx_serialization_coreSerialDescriptor
@required
- (NSArray<id<AssessmentModelKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));
- (id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));
- (int32_t)getElementIndexName:(NSString *)name __attribute__((swift_name("getElementIndex(name:)")));
- (NSString *)getElementNameIndex:(int32_t)index __attribute__((swift_name("getElementName(index:)")));
- (BOOL)isElementOptionalIndex:(int32_t)index __attribute__((swift_name("isElementOptional(index:)")));
@property (readonly) NSArray<id<AssessmentModelKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));
@property (readonly) int32_t elementsCount __attribute__((swift_name("elementsCount")));
@property (readonly) BOOL isNullable __attribute__((swift_name("isNullable")));
@property (readonly) AssessmentModelKotlinx_serialization_coreSerialKind *kind __attribute__((swift_name("kind")));
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreDecoder")))
@protocol AssessmentModelKotlinx_serialization_coreDecoder
@required
- (id<AssessmentModelKotlinx_serialization_coreCompositeDecoder>)beginStructureDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (int32_t)decodeInt __attribute__((swift_name("decodeInt()")));
- (int64_t)decodeLong __attribute__((swift_name("decodeLong()")));
- (BOOL)decodeNotNullMark __attribute__((swift_name("decodeNotNullMark()")));
- (AssessmentModelKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<AssessmentModelKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<AssessmentModelKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
@property (readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerialKind")))
@interface AssessmentModelKotlinx_serialization_coreSerialKind : AssessmentModelBase
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end;

__attribute__((swift_name("KotlinThrowable")))
@interface AssessmentModelKotlinThrowable : AssessmentModelBase
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(AssessmentModelKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(AssessmentModelKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (AssessmentModelKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AssessmentModelKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModule")))
@interface AssessmentModelKotlinx_serialization_coreSerializersModule : AssessmentModelBase
- (void)dumpToCollector:(id<AssessmentModelKotlinx_serialization_coreSerializersModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));
- (id<AssessmentModelKotlinx_serialization_coreKSerializer> _Nullable)getContextualKclass:(id<AssessmentModelKotlinKClass>)kclass __attribute__((swift_name("getContextual(kclass:)")));
- (id<AssessmentModelKotlinx_serialization_coreSerializationStrategy> _Nullable)getPolymorphicBaseClass:(id<AssessmentModelKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));
- (id<AssessmentModelKotlinx_serialization_coreDeserializationStrategy> _Nullable)getPolymorphicBaseClass:(id<AssessmentModelKotlinKClass>)baseClass serializedClassName:(NSString * _Nullable)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end;

__attribute__((swift_name("KotlinError")))
@interface AssessmentModelKotlinError : AssessmentModelKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(AssessmentModelKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(AssessmentModelKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinIterator")))
@protocol AssessmentModelKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end;

__attribute__((swift_name("Kotlinx_serialization_coreCompositeEncoder")))
@protocol AssessmentModelKotlinx_serialization_coreCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (void)encodeIntElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));
- (void)encodeNullableSerializableElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<AssessmentModelKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<AssessmentModelKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)endStructureDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((swift_name("KotlinAnnotation")))
@protocol AssessmentModelKotlinAnnotation
@required
@end;

__attribute__((swift_name("Kotlinx_serialization_coreCompositeDecoder")))
@protocol AssessmentModelKotlinx_serialization_coreCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<AssessmentModelKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<AssessmentModelKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (int16_t)decodeShortElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<AssessmentModelKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
@property (readonly) AssessmentModelKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface AssessmentModelKotlinNothing : AssessmentModelBase
@end;

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModuleCollector")))
@protocol AssessmentModelKotlinx_serialization_coreSerializersModuleCollector
@required
- (void)contextualKClass:(id<AssessmentModelKotlinKClass>)kClass serializer:(id<AssessmentModelKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<AssessmentModelKotlinKClass>)baseClass actualClass:(id<AssessmentModelKotlinKClass>)actualClass actualSerializer:(id<AssessmentModelKotlinx_serialization_coreKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
- (void)polymorphicDefaultBaseClass:(id<AssessmentModelKotlinKClass>)baseClass defaultSerializerProvider:(id<AssessmentModelKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultSerializerProvider __attribute__((swift_name("polymorphicDefault(baseClass:defaultSerializerProvider:)")));
@end;

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol AssessmentModelKotlinKDeclarationContainer
@required
@end;

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol AssessmentModelKotlinKAnnotatedElement
@required
@end;

__attribute__((swift_name("KotlinKClassifier")))
@protocol AssessmentModelKotlinKClassifier
@required
@end;

__attribute__((swift_name("KotlinKClass")))
@protocol AssessmentModelKotlinKClass <AssessmentModelKotlinKDeclarationContainer, AssessmentModelKotlinKAnnotatedElement, AssessmentModelKotlinKClassifier>
@required
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end;

#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
