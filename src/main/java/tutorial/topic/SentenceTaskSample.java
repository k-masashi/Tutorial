package tutorial.topic;
import java.util.List;

import jp.ne.docomo.smt.dev.common.exception.SdkException;
import jp.ne.docomo.smt.dev.common.exception.ServerException;
import jp.ne.docomo.smt.dev.common.http.AuthApiKey;
import jp.ne.docomo.smt.dev.common.http.ProxyInfo;
import jp.ne.docomo.smt.dev.sentenceunderstanding.SentenceTask;
import jp.ne.docomo.smt.dev.sentenceunderstanding.common.SentenceProjectSpecific;
import jp.ne.docomo.smt.dev.sentenceunderstanding.constants.Lang;
import jp.ne.docomo.smt.dev.sentenceunderstanding.data.SentenceAppInfoData;
import jp.ne.docomo.smt.dev.sentenceunderstanding.data.SentenceCommandData;
import jp.ne.docomo.smt.dev.sentenceunderstanding.data.SentenceContentData;
import jp.ne.docomo.smt.dev.sentenceunderstanding.data.SentenceDialogStatusData;
import jp.ne.docomo.smt.dev.sentenceunderstanding.data.SentenceExtractedWordsData;
import jp.ne.docomo.smt.dev.sentenceunderstanding.data.SentenceResultData;
import jp.ne.docomo.smt.dev.sentenceunderstanding.data.SentenceSlotStatusData;
import jp.ne.docomo.smt.dev.sentenceunderstanding.data.SentenceUserUtteranceData;
import jp.ne.docomo.smt.dev.sentenceunderstanding.param.SentenceAppInfoParam;
import jp.ne.docomo.smt.dev.sentenceunderstanding.param.SentenceTaskRequestParam;
import jp.ne.docomo.smt.dev.sentenceunderstanding.param.SentenceUserUtteranceParam;


/**
 * 発話理解サンプルコード
 */
public class SentenceTaskSample {
/*
	private static final String APIKEY = "";
	
	public static void main(String[] args) {

		try {
			// APIKEY の設定
			AuthApiKey.initializeAuth(APIKEY);

			// プロキシの設定
			// プロキシを使用しない場合はコメントにしてください
			ProxyInfo.initializeProxy("proxyhost.co.jp", 80);
			
			// 発話理解パラメータクラスを生成して、発話を設定する
			SentenceTaskRequestParam requestParam = new SentenceTaskRequestParam();

			// アプリケーション情報
			// AppKey は APIKEY を設定
			SentenceAppInfoParam appInfo = new SentenceAppInfoParam();
			appInfo.setAppKey(APIKEY);
			requestParam.setAppInfo(appInfo);

			// クライアントバージョン
			requestParam.setClientVer("1.0");

			// 言語設定
			requestParam.setLanguage(Lang.JP);

			// 発話理解を行う文章を設定
			SentenceUserUtteranceParam userUtterance = new SentenceUserUtteranceParam();
			userUtterance.setUtteranceText("横浜駅から溜池山王駅まで行きたい");
			requestParam.setUserUtterance(userUtterance);

			// アプリ固有情報
			SampleSentenceProjectSpecific project = new SampleSentenceProjectSpecific();
			project.setSampleId("_sampleId");
			requestParam.setProjectSpecific(project);

			// 発話理解クラスを再生してリクエストを実行
			SentenceTask task = new SentenceTask();
			SentenceResultData resultData = task.request(requestParam);

			// 回答データ表示
			System.out.println("プロジェクトキー：" + resultData.getProjectKey());
			System.out.println("クライアントバージョン情報：" + resultData.getClientVer());
			System.out.println("対話モード：" + resultData.getDialogMode());
			System.out.println("言語：" + resultData.getLanguage());
			System.out.println("ユーザ識別情報：" + resultData.getUserId());
			System.out.println("サーバ時刻：" + resultData.getServerSendTime());

			// アプリケーション情報
			SentenceAppInfoData appInfoData = resultData.getAppInfo();
			if (appInfo != null) {
				System.out.println("アプリケーション情報");
				System.out.println("　アプリケーション名：" + appInfoData.getAppName());
				System.out.println("　アプリケーションキー：" + appInfoData.getAppKey());
			}

			// 対話ステータス
			SentenceDialogStatusData dialogStatus = resultData.getDialogStatus();
			if (dialogStatus != null) {
				System.out.println("対話ステータス");
				SentenceCommandData command = dialogStatus.getCommand();
				if (command != null) {
					System.out.println("　コマンド情報");
					System.out.println("　　コマンドID：" + command.getCommandId());
					System.out.println("　　コマンド名称：" + command.getCommandName());
				}
				List<SentenceSlotStatusData> statusList = dialogStatus.getSlotStatusDataList();
				if (statusList != null) {
					System.out.println("　スロット状態");
					for (SentenceSlotStatusData statusData : statusList) {
						System.out.println("　　スロット名称：" + statusData.getSlotName());
						System.out.println("　　スロット値：" + statusData.getSlotValue());
						System.out.println("　　スロット型：" + statusData.getValueType());
					}
				} 
			}

			// 検索結果
			SentenceContentData content = resultData.getContent();
			if (content != null) {
				System.out.println("検索結果");
				System.out.println("　コンテンツ情報：" + content.getContentSource());
				System.out.println("　コンテンツ情報型：" + content.getContentType());
				System.out.println("　コンテンツエンコード情報：" + content.getContentEncode());
				System.out.println("　コンテンツ値：" + content.getContentValue());
			}

			// アプリ固有情報
			SentenceProjectSpecific specific = resultData.getProjectSpecific();
			if (specific != null && specific instanceof SampleSentenceProjectSpecific) {
				SampleSentenceProjectSpecific sampleSpecific = (SampleSentenceProjectSpecific)specific;
				System.out.println("アプリ固有情報");
				System.out.println("　アプリ固有情報：" + sampleSpecific.getSampleId());
			}

			// ユーザ発話内容
			SentenceUserUtteranceData utterance = resultData.getUserUtterance();
			if (utterance != null) {
				System.out.println("ユーザ発話内容");
				System.out.println("　ユーザ発話内容（原文）：" + utterance.getUtteranceText());
				System.out.println("　ユーザ発話内容（ゆらぎ変換）：" + utterance.getUtteranceRevised());
				List<String> wordList = utterance.getUtteranceWordList();
				if (wordList != null) {
					System.out.println("　形態素単語リスト");
					for (String word : wordList) {
						System.out.println("　　" + word);
					}
				}
			}

			// タスク判定結果
			List<String> idList = resultData.getTaskIdList();
			if (idList != null) {
				System.out.println("タスク判定結果");
				for (String id : idList) {
					System.out.println("　" + id);
				}
			}

			// 抽出文字列リスト
			List<SentenceExtractedWordsData> wordsList = resultData.getExtractedWordsList();
			if (wordsList != null) {
				System.out.println("抽出文字列リスト");
				for (SentenceExtractedWordsData words: wordsList) {
					System.out.println("　意図解釈抽出ワード：" + words.getWordsValue());
					List<String> typeList = words.getWordsTypeList();
					if (typeList != null) {
						System.out.println("　意図解釈抽出ワード型");
						for (String type : typeList) {
							System.out.println("　　" + type);
						}
					}
				}
			}
		} catch (SdkException ex) {
			System.out.println("SdkException 発生");
			System.out.println("エラーコード:" + ex.getErrorCode());
			System.out.println("エラーメッセージ:" + ex.getMessage());
			ex.printStackTrace();
		} catch (ServerException ex) {
			System.out.println("ServerException 発生");
			System.out.println("エラーコード:" + ex.getErrorCode());
			System.out.println("エラーメッセージ:" + ex.getMessage());
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	*/
}
