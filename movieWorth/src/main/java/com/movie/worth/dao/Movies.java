package com.movie.worth.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.movie.worth.util.Movie;

public class Movies extends JdbcDaoSupport implements MovieDAO {
	
	private static final String[] GENRES = {"unknown", "Action", "Adventure", "Animation", "Children_s",
			"Comedy", "Crime", "Documentary", "Drama", "Fantasy", "Film_Noir", "Horror", "Musical", 
			"Mystery", "Romance", "Sci_Fi", "Thriller", "War", "Western"};

	//get one movie by it's mid
	public Movie getMovie(int mid) {
		String sql = "SELECT * FROM `movielens`.`items` WHERE mid = ?";
		Movie rs = (Movie) getJdbcTemplate()
				.queryForObject(sql, new Object[]{mid}, new BeanPropertyRowMapper<Movie>(){
					public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
						Movie target = new Movie();
						target.setMid(rs.getInt("mid"));
						target.setTitle(rs.getString("mtitle"));
						target.setReleaseDate(rs.getString("release_date"));
						target.setImdbURL(rs.getString("IMDb_URL"));
						ArrayList<String> genres = new ArrayList<String>();
						for(int i = 0; i < GENRES.length; i++){
							if(rs.getInt(GENRES[i].toString()) == 1){
								genres.add(GENRES[i].toString());
							}
						}
						target.setGenre(genres);
						return target;
					}
				});
		return rs;
	}
	
	//get 5 movie ids from the database
	public List<Integer> get5Movies(int times){
		int limit = 10;
		int offset = times * limit;
		String sql = "SELECT itemid, COUNT(itemid) AS count FROM `movielens`.`ratings` GROUP BY itemid ORDER BY count DESC LIMIT ? OFFSET ?;";
		List<Integer> rs = getJdbcTemplate().query(
				sql,
				new Object[]{limit, offset},
				new RowMapper<Integer>(){
					public Integer mapRow(ResultSet rs, int rowNum) throws SQLException{
						return rs.getInt("itemid");
					}
				});
		return rs;
	}

}