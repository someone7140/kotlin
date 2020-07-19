CREATE TABLE IF NOT EXISTS covit19_summary
(
  prefecture_id integer NOT NULL,
  daily_date bigint NOT NULL,
  population bigint NOT NULL,
  cases bigint NOT NULL,
  deaths bigint NOT NULL,
  pcr bigint NOT NULL,
  hospitalize bigint NOT NULL,
  severe bigint NOT NULL,
  discharge bigint NOT NULL,
  symptom_confirming bigint NOT NULL,
  PRIMARY KEY(prefecture_id, daily_date)
);

CREATE TABLE IF NOT EXISTS covit19_daily
(
  prefecture_id integer NOT NULL,
  daily_date bigint NOT NULL,
  cases bigint NOT NULL,
  deaths bigint NOT NULL,
  pcr bigint NOT NULL,
  hospitalize bigint NOT NULL,
  severe bigint NOT NULL,
  discharge bigint NOT NULL,
  symptom_confirming bigint NOT NULL,
  PRIMARY KEY(prefecture_id, daily_date)
);
