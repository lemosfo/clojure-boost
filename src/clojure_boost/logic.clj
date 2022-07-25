(ns clojure-boost.logic
  (:require [java-time :as jt]))

;Compras realizdas
(defn nova-compra
  "Inserir uma nova compra"
  [data, valor, estabelecimento, categoria, cartao]
  {:data            data
   :valor           valor
   :estabelecimento estabelecimento
   :categoria       categoria
   :cartao          cartao})

;-----------------------------------------------------------
;Calcular o total gasto em compras de um cartão

(defn total-gastos
  "Retorna o total gasto em todos os cartoes"
  [lista-compras]
  (->>
    lista-compras
    (map :valor)
    (reduce +)
    ))

(defn selecionar-cartao
  "Retorna uma lista de gastos para um cartao especifico"
  [numero-cartao lista-compras]
  (->>
    lista-compras
    (filter #(= (get % :cartao) numero-cartao))
    (map :valor)))

(defn total-gastos-por-cartao
  "Retorna o total gasto em um cartao especifico"
  [numero-cartao lista-compras]
  (->>
    (selecionar-cartao numero-cartao lista-compras)
    (reduce +)
    ))

;-----------------------------------------------------------
;Buscar compras por mês

(defn splitar-mes
  "funcao que transforma a data em um mes (inteiro)"
  [lista-compras]
  (->
    (clojure.string/split lista-compras #"-")
    (get 1)
    (Integer/parseInt)))

(defn obter-compras-por-mes
  "funcao para obter todas as compras baseado em um mes (inteiro)"
  [lista-compras mes]
  (->>
    lista-compras
    (filter #(= mes (splitar-mes (:data %))))))

;-----------------------------------------------------------
;Calcular o total da fatura de um mês

(defn total-gasto-no-mes
  "funcao para calcular o total de gastos em um mes para um cartao especifico"
  [lista-compras mes cartao]
  (->>
    (selecionar-cartao cartao (obter-compras-por-mes lista-compras mes))
    (reduce +)))

;-----------------------------------------------------------

(defn obtem-categorias
  "funcao para obter as categorias e os valores individuais"
  [lista-compras]
  (->> lista-compras
       (group-by :categoria)))

(defn agrupar-por-categoria
  "funcao que retorna o total por categoria"
  [lista-compras]
  (into (sorted-map) (map (fn [[categoria compras]]
                            [categoria (total-gastos compras)])
                          (obtem-categorias lista-compras))))

;-----------------------------------------------------------

(defn filtra-intervalo-valor
  "funcao para filtrar um intervalo de valores"
  [lista-compras valor-minimo valor-maximo]
  (->> lista-compras
       (map :valor)
       (filter #(and (>= % valor-minimo) (<= % valor-maximo)))
       ))

;-----------------------------------------------------------

(defn convert-datas-cartao
  "Funcao para converter em datas os dados de validade do cartao de credito"
  [cartoes]
  (->> cartoes
       (map #(update % :validade jt/year-month))))

(defn convert-datas-compras
  "Funcao para converter em datas os dados de validade do cartao de credito"
  [lista-compras]
  (->> lista-compras
       (map #(update % :data jt/local-date))))

