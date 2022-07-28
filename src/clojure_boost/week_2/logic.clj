(ns clojure-boost.week-2.logic
  (:use [clojure.pprint])
  (:require [clojure-boost.week_2.utils :as utils.week_2]))

(defrecord compra [ID data valor estabelecimento categoria cartao])
(defrecord compra-otimizada [^Long ID ^String data ^BigDecimal valor ^String estabelecimento ^String categoria ^Long cartao])

(time (pprint (compra. nil "25-10-2022" 10.00 "pp" "games" 1234123412341234)))

;-------------------------------------------------------------------------------------------------------------------
;Criar a função "insere-compra". Ela vai atribuir um "id" a uma compra e armazená-la num vetor.

(defn gera-id
  "Funcao para gerar um ID de compra.
  Ela espera receber uma colecao"
  [compras]
  (let [id-gerado (last (sort (map #(get % :ID) compras)))]
    (if (nil? id-gerado)
      (int 1)
      (inc id-gerado))))

(swap! repositorio-de-compras gera-id)

(defn insere-compra
  "Funcao para inserir uma nova compra e atribuir um ID para essa compra caso seja necessario.
  A Funcao espera receber uma compra e uma coleção de compras"
  [compras nova-compra]
  (->>
    (conj compras (assoc nova-compra :ID (gera-id compras)))
    ))

;Teste:
(pprint (insere-compra (compra. nil "25-10-2022" 1000.00 "Games Mania" "games" 1234123412341234) repositorio-de-compras))
(pprint (insere-compra utils.week_2/compras-exemplo (compra. 140 "25-10-2022" 1000.00 "Games Mania" "games" 1234123412341234)))
(pprint (insere-compra (compra. nil "25-10-2021" 109.99 "Fliperama do Zé" "games" 1234124212341234) utils.week_2/compras-vazio))

;Observacoes:
;- Nao tem logica para tratar compras repetidas
;- Nao tem logica para tratar colecoes de compras que contem IDs nil (vazios) registrados previamente

;---------------------------------------------------------------------------------------------------------
;Criar a função "insere-compra!" para incluir uma nova compra no átomo de compras usando "swap!".

(defn insere-compra!
  "Funcao para inserir uma nova compra no atomo."
  [nova-compra compras]
  (swap! compras insere-compra nova-compra)
  )

(pprint (insere-compra! (->compra nil "25-10-2022" 32.00 "Lojinha do seu Ze" "Pub" 1234123412341234) utils.week_2/repositorio-de-compras))
;---------------------------------------------------------------------------------------------------------
;(pprint (get (compra. 2 "25-10-2022" 10.00 "pp" "games" 1234123412341234) :ID))
;
;(def compras-vazio [])
;
;(defn gera-id-nova-compra [nova-compra]
;  ())
;;com map
;(defn gera-id-compras-da-base [compras]
;  (let [identifica-compras (map #(get % :ID) compras)]
;    (pprint identifica-compras)
;    )
;  )
;;if map
;(defn gera-id-compras-da-base [compras]
;  (let [identifica-compras (map #(if (nil? (get % :ID)) (conj % {:ID 1}) (pprint "Já possui id")) compras)]
;    (pprint compras)
;    ) compras
;  )
;
;(defn x [compras]
;  (->>
;    (map #(if (nil? (get % :ID)) (conj % [:ID 1]) (pprint "Já possui id")) compras)
;    ;compras
;    ) compras)
;
;(x compras-exemplo)
;(gera-id-compras-da-base compras-exemplo)
;(pprint (gera-id-compras-da-base-th compras-exemplo))

;(def compras-cadastradas (vec utils.week_1/lista-compras))
;(pprint compras-cadastradas)