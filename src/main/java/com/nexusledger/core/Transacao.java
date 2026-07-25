package com.nexusledger.core;

import java.time.LocalDateTime;
import java.util.UUID;
/*
 Registro imutável (DTO) que representa uma movimentação financeira no extrato da conta.
 O uso do Java Record garante conformidade e segurança de auditoria ao impedir que os dados
 de uma transação sejam alterados após emitidos, gerando a infraestrutura de leitura automaticamente.
 */

public record Transacao(UUID id, LocalDateTime timestamp, String tipoOperacao, double valor) {
}
