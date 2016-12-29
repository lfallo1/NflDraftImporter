ALTER TABLE player
  ADD CONSTRAINT player_unique_name_yr UNIQUE(name, year);
  
CREATE INDEX player_idx
  ON player
  USING btree
  (id);