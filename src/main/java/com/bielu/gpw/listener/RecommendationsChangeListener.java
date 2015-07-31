package com.bielu.gpw.listener;

import java.util.List;

import com.bielu.gpw.domain.Recommendation;

public interface RecommendationsChangeListener {
  void recommendationsChanged(List<Recommendation> recommendation);
}
