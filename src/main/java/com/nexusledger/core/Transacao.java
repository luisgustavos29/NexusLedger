package com.nexusledger.core;

import java.time.LocalDateTime;
import java.util.UUID;

public record Transacao(UUID id, LocalDateTime timestamp, String tipoOperacao, double valor) {
}
