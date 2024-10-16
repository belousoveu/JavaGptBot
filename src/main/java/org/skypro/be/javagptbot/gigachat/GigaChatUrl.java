package org.skypro.be.javagptbot.gigachat;

public class GigaChatUrl {
    public static final String AUTHORIZATION_URL = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
    public static final String MODELS_URL = "https://gigachat.devices.sberbank.ru/api/v1/models";
    public static final String GET_ANSWER_URL = "https://gigachat.devices.sberbank.ru/api/v1/chat/completions";
    public static final String SEND_FILE_URL = "https://gigachat.devices.sberbank.ru/api/v1/files";
    public static final String GET_FILE_LIST_URL = "https://gigachat.devices.sberbank.ru/api/v1/files";
    public static final String GET_PICTURE_URL = "https://gigachat.devices.sberbank.ru/api/v1/files/{file_id}/content";
    public static final String GET_FILE_INFO_URL = "https://gigachat.devices.sberbank.ru/api/v1/files/{file_id}";
    public static final String DELETE_FILE_URL = "https://gigachat.devices.sberbank.ru/api/v1/files/{file_id}/delete";
    public static final String CREATE_EMBEDDINGS_URL ="https://gigachat.devices.sberbank.ru/api/v1/embeddings";
    public static final String GET_TOKENS_COUNT_URL = "https://gigachat.devices.sberbank.ru/api/v1/tokens/count";

}
